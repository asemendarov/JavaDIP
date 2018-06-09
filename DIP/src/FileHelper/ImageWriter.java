package FileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    public static void execute(BufferedImage image, String format, File file) throws IOException {
        exists(file);

        // pass

        ImageIO.write(image, format, file);
    }

    private static void exists(File file) throws IOException {
        if(!file.exists())
            file.createNewFile();
    }

}
