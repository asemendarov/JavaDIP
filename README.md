# Digital Image Processing — Цифровая обработка изображений ([Pages](https://onocomments.github.io/JavaDIP/))

#### Утановка OpenCV
0. В директории `...\JavaDIP\resources\modules\opencv` имеются все необходимы файлы OpenCV.
1. Для подключения модуля OpenCV выполните следующую инструкцию ([IDE Eclipse](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html))

IntelliJ IDEA:

    1. File -> Project Structure [Ctrl + Alt + Shift + S]
    2. Project Settings -> Modules -> Dependencies -> Add -> JARs or directories...
    3. Set path %USERPROFILE%\IdeaProjects\JavaDIP\resources\modules\opencv\opencv-341.jar -> Click OK
    4. Edit module opencv-341.jar -> Add
    5. Set path %USERPROFILE%\IdeaProjects\JavaDIP\resources\modules\opencv\x64 (or x86) -> Click OK
    7. Click OK

#### Выдержки из ExampleJavaFX
0. Чтение image файла:

        // Реализация 1
        File sourceimage = new File("source.gif");
        image = ImageIO.read(sourceimage);
        
        // Реализация 2
        InputStream is = new BufferedInputStream(new FileInputStream("source.gif"));
        image = ImageIO.read(is);
        
        // Реализация 3
        URL url = new URL("http://java-tips.org/source.gif");
        image = ImageIO.read(url);

2. Получение значения каждого пикселя

        c.getRed();
        c.getGreen();
        c.getBlue();

3. Получение ширины и высоты изображения

        int width = image.getWidth();
        int height = image.getHeight();

4. Чтобы вернуть тип изображения у класса Image есть метод GetType()

        String format = image.GetType();

5. GrayScale преобразования

        Color c = new Color(image.getRGB(j, i));
        int red = (int)(c.getRed() * 0.299);
        int green = (int)(c.getGreen() * 0.587);
        int blue = (int)(c.getBlue() *0.114);
        Color newColor = new Color(
            red+green+blue,
            red+green+blue,
            red+green+blue
        );
        image.setRGB(j,i,newColor.getRGB());

6. ...

#### Выдержки из ExampleOpenCV

0. Обязательное наличие статического блока инициализации для работы с OpenCV
    
        static {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        }

1. Чтение image файла:
    
        Mat image = Imgcodecs.imread("resources/image/poli.jpg");

2. Запись image файла:
    
        Imgcodecs.imwrite("resources/image/poli-gray.jpg", image);

3. GrayScale преобразования
    
        Imgcodecs.imwrite("resources/image/poli-gray.jpg", image);

4. Создание и печать на экране матрицы 3x3

        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());

5. Чтение Image и вставка в ImageView средствами
    
        imageView.setImage(FXTool.mat2Image(".jpg", Imgcodecs.imread("resources/image/poli.jpg")))
        
        или
            imageView.setImage(FXTool.mat2ImageAlternative(Imgcodecs.imread("resources/image/poli.jpg")));
        или
            imageView.setImage(new Image(new BufferedInputStream(new FileInputStream("resources/image/poli.jpg"))))

6. ...