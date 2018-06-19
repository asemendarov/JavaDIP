package DiscreteFourierTransform.WindowThread;

import Tools.FXTool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    @FXML
    public ImageView transformedImage;
    @FXML
    private Button buttonStart;

    @FXML
    private ImageView originalImage;

    @FXML
    private Label lableInf;

    private VideoCapture capture = new VideoCapture();
    private ScheduledExecutorService timer;
    private boolean cameraActive = false;
    private static int CAMERA_ID = 0;

    private Mat frame;
    private List<Mat> planes;
    private Mat complexImage;

    {
        frame = new Mat();
        planes = new ArrayList<>();
        complexImage = new Mat();
    }

    @FXML
    public void run(ActionEvent actionEvent) {
        try {
            if (!cameraActive){
                startCamera();
                cameraActive = true;
                buttonStart.setText("Stop Camera");
            }
            else {
                stopCamera();
                cameraActive = false;
                buttonStart.setText("Start Camera");
            }

        } catch (Exception ex){
            lableInf.setText(ex.getMessage());
        }
    }

    private void startCamera() throws IOException {

        capture.open(CAMERA_ID);

        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        Runnable runnable = () -> {
            try {
                loadImage(capture);
                transformImage();
            } catch (Exception ex){
                updateLableText(ex.getMessage());
            }
        };

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(runnable, 0, 63, TimeUnit.MILLISECONDS);
    }

    private void loadImage(VideoCapture capture){
        capture.read(frame);

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);

//        updateImageView(originalImage,  FXTool.mat2ImageAlternative(frame));
        updateImageView(originalImage,  FXTool.mat2Image(".jpg", frame));
    }

    private void transformImage(){
        planes = new ArrayList<>();
        complexImage = new Mat();

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

//        updateImageView(transformedImage, FXTool.mat2ImageAlternative(magnitude));
        updateImageView(transformedImage, FXTool.mat2Image(".jpg", magnitude));
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

    private void stopCamera() throws IOException {
        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        if (timer != null && !timer.isShutdown()){
            this.timer.shutdown();

            try {
                this.timer.awaitTermination(63, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                lableInf.setText(e.getMessage());
            }
        }

        capture.release();
    }

    public void stop() throws IOException {
        stopCamera();
    }

    public void setLableInf(String text) {
        lableInf.setText(text);
    }

    private void updateImageView(ImageView imageView, Image image) {
        Platform.runLater(() -> {
            imageView.imageProperty().set(image);
        });
    }

    private void updateLableText(String text){
        Platform.runLater(() -> {
            lableInf.setText(text);
        });
    }
}