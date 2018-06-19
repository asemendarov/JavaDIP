package DiscreteFourierTransform.Window;

import Tools.FXTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    public ImageView originalImage;
    @FXML
    public ImageView transformedImage;
    @FXML
    public ImageView antitransformedImage;

    @FXML
    public Button transformButton;
    @FXML
    public Button antitransformButton;

    @FXML
    public Label lableInfo;

    private Mat frame;
    private List<Mat> planes;
    private Mat complexImage;

    private File file;
    private Stage stageWindow;
    private FileChooser fileChooser;

    {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources/image"));
        frame = new Mat();

        planes = new ArrayList<>();
        complexImage = new Mat();
    }

    @FXML
    public void loadImage(ActionEvent actionEvent) {
        try {
            isStageWindow();

            file = fileChooser.showOpenDialog(stageWindow);

            if (file == null)
                throw new FileNotFoundException("Пожалуйста, определите File");

            frame = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

            if (frame.empty())
                throw new IOException("Error: current frame is empty");

            originalImage.setImage(FXTool.mat2Image(getFileExtension(file), frame));

            planes = new ArrayList<>();
            complexImage = new Mat();

            transformButton.setDisable(false);
            antitransformButton.setDisable(false);
            transformedImage.setImage(null);
            antitransformedImage.setImage(null);
        } catch (Exception ex) {
            lableInfo.setText(String.format("Error: %s", ex.getMessage()));
        }
    }

    @FXML
    public void transformImage(ActionEvent actionEvent) {
        try {
            // оптимизировать размер загруженного изображения
            Mat padded = optimizeImageDim(frame);
            padded.convertTo(padded, CvType.CV_32F);
            // подготавливаем плоскости изображения для получения сложного изображения
            planes.add(padded);
            planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
            // подготавливаем сложное изображение для выполнения dft
            Core.merge(planes, complexImage);

            // выполняем dtf
            Core.dft(complexImage, complexImage);

            // оптимизируем изображение, полученное в результате операции dft
            Mat magnitude = createOptimizedMagnitude(complexImage);

            transformedImage.setImage(FXTool.mat2Image(getFileExtension(file), magnitude));

            transformButton.setDisable(true);
            antitransformButton.setDisable(false);
        } catch (Exception ex) {
            lableInfo.setText(String.format("Error: %s", ex.getMessage()));
        }
    }

    @FXML
    public void antitransformImage(ActionEvent actionEvent) {
        try {
            Core.idft(complexImage, complexImage);

            Mat restoredImage = new Mat();
            Core.split(complexImage, planes);
            Core.normalize(planes.get(0), restoredImage, 0, 255, Core.NORM_MINMAX);

            restoredImage.convertTo(restoredImage, CvType.CV_8U);

            antitransformedImage.setImage(FXTool.mat2Image(getFileExtension(file), restoredImage));

            transformButton.setDisable(true);
            antitransformButton.setDisable(true);
        } catch (Exception ex) {
            lableInfo.setText(String.format("Error: %s", ex.getMessage()));
        }
    }

    private Mat optimizeImageDim(Mat image) {
        Mat padded = new Mat();

        int addPixelRows = Core.getOptimalDFTSize(image.rows());
        int addPixelCols = Core.getOptimalDFTSize(image.cols());

        Core.copyMakeBorder(image, padded, 0, addPixelRows - image.rows(), 0, addPixelCols - image.cols(),
                Core.BORDER_CONSTANT, Scalar.all(0));

        return padded;
    }

    private Mat createOptimizedMagnitude(Mat complexImage) {
        List<Mat> newPlanes = new ArrayList<>();
        Mat mag = new Mat();

        Core.split(complexImage, newPlanes);
        Core.magnitude(newPlanes.get(0), newPlanes.get(1), mag);

        Core.add(Mat.ones(mag.size(), CvType.CV_32F), mag, mag);
        Core.log(mag, mag);

        this.shiftDFT(mag);

        mag.convertTo(mag, CvType.CV_8UC1);
        Core.normalize(mag, mag, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

        // вы также можете сохранить результирующее изображение ...
        // Imgcodecs.imwrite("../magnitude.png", mag);

        return mag;
    }

    // Переупорядочить 4 квадранта изображения, представляющего величину, после DFT
    private void shiftDFT(Mat image) {
        image = image.submat(new Rect(0, 0, image.cols() & -2, image.rows() & -2));
        int cx = image.cols() / 2;
        int cy = image.rows() / 2;

        Mat q0 = new Mat(image, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(image, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(image, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(image, new Rect(cx, cy, cx, cy));

        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);

        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);
    }

    public void setStageWindow(Stage stageWindow) {
        this.stageWindow = stageWindow;
    }

    private void isStageWindow() {
        if (stageWindow == null)
            throw new InvalidParameterException("Пожалуйста, определите Stage Window");
    }

    private static String getFileExtension(File file) {
        if (!file.isFile())
            return null;

        String fileName = file.getName();

        if (fileName.lastIndexOf(".") == -1)
            return null;

        return fileName.substring(fileName.lastIndexOf("."));
    }
}