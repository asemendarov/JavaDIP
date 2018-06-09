package FileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageReader {

    private static BufferedImage bufferedImage;

    public static BufferedImage execute(File file) throws IOException {
        exists(file);

        // pass

        bufferedImage = ImageIO.read(file);
        return bufferedImage;
    }

    private static void exists(File file) throws FileNotFoundException {
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    public static BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
