package ExampleClassImage;

import ExampleClassImage.ImageFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ImageFrame frame = new ImageFrame("DIP\\image\\cat.jpg");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}

/*
0. Чтение image файла:
    image = ImageIO.read(new File(filname));

1. Значение пикселя можно получить, используя следующий синтаксис:
    Color c = new Color(image.getRGB(j, i));

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

6.
 */