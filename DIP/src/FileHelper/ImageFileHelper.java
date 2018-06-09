package FileHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFileHelper {

    private File file;

    public ImageFileHelper(File file) {
        setFile(file);
    }

    public ImageFileHelper(String fileName) {
        setFile(fileName);
    }

    public void write(BufferedImage image) throws IOException {
        ImageWriter.execute(image, "png", file);
    }

    public BufferedImage read() throws IOException {
        return ImageReader.execute(file);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String fileName){
        this.file = new File(System.getProperty("user.dir")
                + File.separator + fileName);
        System.out.println(file);
    }
}
