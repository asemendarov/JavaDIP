package ExampleOpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

    static int width;
    static int height;
    static double alpha = 2;
    static double beta = 50;

    static {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }

    public static void main(String[] args) {
        // создаем и печатаем на экране матрицу 3x3
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());

        // Чтение image файла:
        Mat image = Imgcodecs.imread("resources/image/poli.jpg");

        // GrayScale преобразования
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        // Запись image файла:
        Imgcodecs.imwrite("resources/image/poli-gray.jpg", image);
    }
}