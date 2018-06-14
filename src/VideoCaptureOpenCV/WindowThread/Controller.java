package VideoCaptureOpenCV.WindowThread;

import Tools.FXTool;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Controller {

    @FXML
    private Button buttonStart;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lableInf;

    private VideoCapture capture = new VideoCapture();
    private ScheduledExecutorService timer;
    private boolean cameraActive = false;
    private static int CAMERA_ID = 0;

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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Mat frame = new Mat();

                capture.read(frame);

//                if (frame.empty())
//                    throw new IOException("Error: current frame is empty");

                Image image = FXTool.mat2Image(".jpg", frame);

                Platform.runLater(() -> {
                    imageView.imageProperty().set(image);
                });
            }
        };

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(runnable, 0, 63, TimeUnit.MILLISECONDS);
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
}