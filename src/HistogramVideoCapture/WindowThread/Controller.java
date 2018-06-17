package HistogramVideoCapture.WindowThread;

import Tools.FXTool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    private static int CAMERA_ID = 0;
    @FXML
    public CheckBox grayscaleCheckBox;
    @FXML
    public CheckBox logoCheckBox;
    @FXML
    public ImageView histogramImageView;
    @FXML
    private Button buttonStart;
    @FXML
    private ImageView imageView;
    @FXML
    private Label lableInf;
    private VideoCapture capture = new VideoCapture();
    private ScheduledExecutorService timer;
    private boolean cameraActive = false;
    private Mat logo;

    @FXML
    public void run(ActionEvent actionEvent) {
        try {
            if (!cameraActive) {
                startCamera();
                cameraActive = true;
                lableInf.setText("Start Camera");
                buttonStart.setText("Stop Camera");
            } else {
                stopCamera();
                cameraActive = false;
                lableInf.setText("Stop Camera");
                buttonStart.setText("Start Camera");
            }

        } catch (Exception ex) {
            lableInf.setText(ex.getMessage());
        }
    }

    private void startCamera() throws IOException {

        capture.open(CAMERA_ID);

        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        Runnable runnable = () -> {
            Mat frame = new Mat();

            capture.read(frame);

            if (grayscaleCheckBox.isSelected()) {
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
            }

            if (logoCheckBox.isSelected() && logo != null && !grayscaleCheckBox.isSelected()) {
                int margin = 10;
                Rect ROI = new Rect(
                        frame.cols() - logo.cols() - margin,
                        frame.rows() - logo.rows() - margin,
                        logo.cols(),
                        logo.rows()
                );
                Mat imageROI = frame.submat(ROI);
                Core.addWeighted(imageROI, 1.0, logo, 0.7, 0.0, imageROI);
            }

            Image image = FXTool.mat2Image(".jpg", frame);

            updateImageView(imageView, image);

            showHistogram(frame);
        };

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(runnable, 0, 63, TimeUnit.MILLISECONDS);
    }

    private void stopCamera() throws IOException {
        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        if (timer != null && !timer.isShutdown()) {
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

    public void loadLogo(ActionEvent actionEvent) {
        if (logoCheckBox.isSelected())
            logo = Imgcodecs.imread("resources/image/cat.jpg");
        else
            logo = null;
    }

    private void showHistogram(Mat frame) {
        // split the frames in multiple images
        List<Mat> imageList = new ArrayList<Mat>();
        Core.split(frame, imageList);

        // set the number of bins at 255
        MatOfInt histSize = new MatOfInt(255);
        // only one channel
        MatOfInt channels = new MatOfInt(0);
        // set the ranges
        MatOfFloat histRange = new MatOfFloat(0, 255);

        // compute the histograms for the B, G and R components
        Mat hist_b = new Mat();
        Mat hist_g = new Mat();
        Mat hist_r = new Mat();

        // B component or gray image
        Imgproc.calcHist(imageList.subList(0, 1), channels, new Mat(), hist_b, histSize, histRange, false);

        // G and R components (if the image is not in gray scale)
        if (!grayscaleCheckBox.isSelected()) {
            Imgproc.calcHist(imageList.subList(1, 2), channels, new Mat(), hist_g, histSize, histRange, false);
            Imgproc.calcHist(imageList.subList(2, 3), channels, new Mat(), hist_r, histSize, histRange, false);
        }

        // draw the histogramImageView
        int hist_w = (int) histogramImageView.getFitWidth(); // width of the histogramImageView image
        int hist_h = (int) histogramImageView.getFitHeight(); // height of the histogramImageView image
        int bin_w = (int) Math.round(hist_w / histSize.get(0, 0)[0]);

        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));
        // normalize the result to [0, histImage.rows()]
        Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());

        // for G and R components
        if (!grayscaleCheckBox.isSelected()) {
            Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
            Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
        }

        // effectively draw the histogramImageView(s)
        for (int i = 1; i < histSize.get(0, 0)[0]; i++) {
            // B component or gray image
            Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(i - 1, 0)[0])),
                    new Point(bin_w * (i), hist_h - Math.round(hist_b.get(i, 0)[0])), new Scalar(255, 0, 0), 2, 8, 0);
            // G and R components (if the image is not in gray scale)
            if (!grayscaleCheckBox.isSelected()) {
                Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_g.get(i, 0)[0])), new Scalar(0, 255, 0), 2, 8,
                        0);
                Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_r.get(i, 0)[0])), new Scalar(0, 0, 255), 2, 8,
                        0);
            }
        }

        // display the histogramImageView...
        updateImageView(histogramImageView, FXTool.mat2Image(".jpg", histImage));
    }

    private void updateImageView(ImageView imageView, Image image) {
        Platform.runLater(() -> {
            imageView.imageProperty().set(image);
        });
    }
}