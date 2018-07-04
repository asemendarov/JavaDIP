### Digital Image Processing — Цифровая обработка изображений ([io](https://onocomments.github.io/JavaDIP/))

### Утановка OpenCV
0. В директории `...\JavaDIP\resources\modules\opencv` имеются все необходимы файлы OpenCV.
1. Для подключения модуля OpenCV выполните следующую инструкцию ([IDE Eclipse](http://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html))

```
IntelliJ IDEA:

0. File -> Project Structure [Ctrl + Alt + Shift + S]
1. Project Settings -> Modules -> Dependencies -> Add -> JARs or directories...
2. Set path %USERPROFILE%\IdeaProjects\JavaDIP\resources\modules\opencv\opencv-341.jar -> Click OK
3. Edit module opencv-341.jar -> Add
4. Set path %USERPROFILE%\IdeaProjects\JavaDIP\resources\modules\opencv\x64 (or x86) -> Click OK
5. Click OK
```

### Пакет ExampleJavaFX
Очень простое JavaFX приложение, **без** использования fxmi и OpenCV, демонстрирующее работу с пакетом `javax.imageio.ImageIO`

![ExampleJavaFX](https://user-images.githubusercontent.com/31689842/42292159-32d0e70e-7fd9-11e8-9e23-7f82a6bf3730.png)

### Пакет ExampleOpenCV
Очень простое JavaFX приложение, **c** использования fxmi и OpenCV, демонстрирующее работу с пакетом `org.opencv.imgcodecs.Imgcodecs`

![ExampleOpenCV](https://user-images.githubusercontent.com/31689842/42292631-856a61a4-7fdc-11e8-87f7-b716cd945d76.png)

### Пакет VideoCaptureOpenCV
Приложение чуть посложней, но не особо. В этом пакете и в последующих используется fxmi и OpenCV.
Данная работа демонстрирует то, как можно захватывать  видеопоток с веб-камеры, а затем отображать его на пользовательском интерфейсе (GUI). Имеется две реализации: без многопоточности (просто захватываем один кадр и отображаем на GUI) и с многопоточностью (полноценное отображаение видеопотока на GUI).

![VideoCaptureOpenCV](https://user-images.githubusercontent.com/31689842/42293409-a901e01e-7fe2-11e8-9f5a-8d8676b65820.gif)

### Пакет HistogramVideoCapture
В этом приложении реализаванно: изменение цвета видеопотока, добавление логотипа поверх видеопотока, отображение гистограммы видеопотока (как одиного, так и трех каналов).

![HistogramVideoCapture](https://user-images.githubusercontent.com/31689842/42293680-a445c66a-7fe4-11e8-8b1f-77f849125425.gif)

### Пакет FaceDetectionAndTracking
В этом приложении демонстрируется использование классификатор Хаара и классификатор LBP, которые уже прошли обучение (распространяются OpenCV) для обнаружения и отслеживания движущегося лица в видеопотоке.

![FaceDetectionAndTracking](https://user-images.githubusercontent.com/31689842/42294285-3721b6b2-7fe8-11e8-9bc0-9241bf97fd20.gif)
