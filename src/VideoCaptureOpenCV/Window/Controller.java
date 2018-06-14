package VideoCaptureOpenCV.Window;

import Tools.FXTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;


public class Controller {

    @FXML
    public Button buttonStart;

    @FXML
    public ImageView imageView;

    @FXML
    public Label lableInf;

    private VideoCapture capture = new VideoCapture();

    @FXML
    public void run(ActionEvent actionEvent) {
        try {

            capture.open(0);

            if (!capture.isOpened())
                throw new IOException("Error: video stream unavailable");

            Mat frame = new Mat();

            capture.read(frame);

            if (frame.empty())
                throw new IOException("Error: current frame is empty");

            imageView.setImage(FXTool.mat2Image(".jpg", frame));

        } catch (Exception ex){
            lableInf.setText(ex.getMessage());
        }
    }

    public void stop(){
        if (capture.isOpened())
            capture.release();
    }
}