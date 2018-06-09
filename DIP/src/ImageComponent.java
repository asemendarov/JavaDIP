import FileHelper.ImageFileHelper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImageComponent extends JComponent {

    private Image image;

    public ImageComponent(String filname) throws IOException {
        image = new ImageFileHelper(filname).read();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        System.out.println(super.getWidth() + " " + super.getHeight());

        // Отображение рисунка в левом верхнем углу.
        g.drawImage(image, 0, 0, null);

        // Многократный вывод изображения в панели.

        for(int i = 0; i * imageWidth <= getWidth(); i++)
            for(int j = 0; j * imageHeight <= getHeight(); j++)
                if(i + j > 0)
                    g.copyArea(0, 0, imageWidth, imageHeight, i * imageWidth, j * imageHeight);
    }
}
