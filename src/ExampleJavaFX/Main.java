package ExampleJavaFX;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ImageFrame frame = new ImageFrame("resources/image/cat.jpg");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }
}