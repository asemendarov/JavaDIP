package ExampleJavaFX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComponent extends JComponent {

    private BufferedImage image;

    public ImageComponent(String filname) throws IOException {
        image = ImageIO.read(new File(filname));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if(image == null)
            return;

        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        System.out.println(super.getWidth() + " " + super.getHeight());

        // Отображение рисунка в левом верхнем углу.
        g.drawImage(image, (super.getWidth() - imageWidth)/2,
                (super.getHeight() - imageHeight)/2, null);

    }
}
