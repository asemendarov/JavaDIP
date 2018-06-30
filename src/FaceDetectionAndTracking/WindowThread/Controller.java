package FaceDetectionAndTracking.WindowThread;

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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    @FXML
    public CheckBox checkBoxHaarClassifier;
    @FXML
    public CheckBox checkBoxLBPClassifier;
    @FXML
    private ImageView imageView;
    @FXML
    private Button buttonStart;
    @FXML
    private Label lableInf;

    // Classifier
    private final CascadeClassifier faceCascade = new CascadeClassifier();
    private int absoluteFaceSize = 0;

    // VideoCapture
    private final VideoCapture capture = new VideoCapture();
    private boolean cameraActive = false;
    private final int CAMERA_ID = 0;
    private Mat frame = new Mat();

    // Thread
    private ScheduledExecutorService timer;
    private final long TIMER_INITIAL_DELAY = 0;
    private final long TIMER_PERIOD = 63;

    @FXML
    public void run(ActionEvent actionEvent) {
        if (!checkBoxHaarClassifier.isSelected() && !checkBoxLBPClassifier.isSelected()){
            lableInf.setText("Пожалуйста, выберите метод классификации!");
            return;
        }

        lableInf.setText("");

        try {
            if (!cameraActive) {
                startCamera();
                cameraActive = true;
                setCheckBoxDisable(true, true);
                buttonStart.setText("Stop Camera");
            } else {
                stopCamera();
                cameraActive = false;
                setCheckBoxDisable(false, false);
                buttonStart.setText("Start Camera");
            }

        } catch (Exception ex) {
            lableInf.setText(ex.getMessage());
            rollback();
        }
    }

    private void rollback(){
        // pass
    }

    private void startCamera() throws IOException {

        capture.open(CAMERA_ID);

        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        Runnable runnable = () -> {
            try {
                capture.read(frame);

                detectAndDisplay(frame);

                updateImageView(imageView,  FXTool.mat2Image(".jpg", frame));
            } catch (Exception ex) {
                updateLableText(ex.getMessage());
            }
        };

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(runnable, TIMER_INITIAL_DELAY, TIMER_PERIOD, TimeUnit.MILLISECONDS);
    }

    private void stopCamera() throws IOException {
        if (!capture.isOpened())
            throw new IOException("Error: video stream unavailable");

        if (timer != null && !timer.isShutdown()) {
            this.timer.shutdown();

            try {
                this.timer.awaitTermination(TIMER_PERIOD, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                lableInf.setText(e.getMessage());
            }
        }

        capture.release();
    }

    public void stop() throws IOException {
        stopCamera();
    }

    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // конвертируем изображение в серый
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // выравниваем гистограмму
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // определяем минимальный размер лица
        if (absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // обнаруживаем лицо
        faceCascade.detectMultiScale(
                grayFrame,
                faces,
                1.1,
                2,
                Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize),
                new Size()
        );

        // отображаем области, где было найдено лицо
        for (Rect aFacesArray : faces.toArray())
            Imgproc.rectangle(frame, aFacesArray.tl(), aFacesArray.br(), new Scalar(0, 0, 255), 5);

    }

    @FXML
    public void haarSelected(ActionEvent actionEvent) {
        if (!checkBoxHaarClassifier.isSelected()) return;

        checkBoxLBPClassifier.setSelected(false);

        try {
            faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");
        } catch (Exception ex) {
            checkBoxHaarClassifier.setSelected(false);
            lableInf.setText(ex.getMessage());
        }
    }

    @FXML
    public void lbpSelected(ActionEvent actionEvent) {
        if (!checkBoxLBPClassifier.isSelected()) return;

        checkBoxHaarClassifier.setSelected(false);

        try {
            faceCascade.load("resources/lbpcascades/lbpcascade_frontalface.xml");
        } catch (Exception ex) {
            checkBoxLBPClassifier.setSelected(false);
            lableInf.setText(ex.getMessage());
        }
    }

    private void setCheckBoxDisable(boolean haarClassifier, boolean lbpClassifier){
        checkBoxHaarClassifier.setDisable(haarClassifier);
        checkBoxLBPClassifier.setDisable(lbpClassifier);
    }

    public void setLableInf(String text) {
        lableInf.setText(text);
    }

    private void updateImageView(ImageView imageView, Image image) {
        Platform.runLater(() -> {
            imageView.imageProperty().set(image);
        });
    }

    private void updateLableText(String text) {
        Platform.runLater(() -> {
            lableInf.setText(text);
        });
    }
}
