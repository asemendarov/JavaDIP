package ExampleJavaFX;

import javax.swing.*;
import java.io.IOException;

public class ImageFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    public ImageFrame(String fileName) throws IOException {
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Добавление компонента к фрейму.
        add(new ImageComponent(fileName));
    }
}
