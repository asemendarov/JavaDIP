package DiscreteFourierTransform.Window;

import Tools.FXTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.security.InvalidParameterException;

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

    private Stage stageWindow;
    private FileChooser fileChooser;

    {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources/image"));
    }

    @FXML
    public void loadImage(ActionEvent actionEvent) {
        try {
            isStageWindow();

            File file = fileChooser.showOpenDialog(stageWindow);

            if (file == null)
                throw new FileNotFoundException("Пожалуйста, определите File");

            Mat frame = Imgcodecs.imread(file.getAbsolutePath());

            if (frame.empty())
                throw new IOException("Error: current frame is empty");

            originalImage.setImage(FXTool.mat2Image(getFileExtension(file), frame));

        } catch (Exception ex) {
            lableInfo.setText(ex.getMessage());
        }
    }

    @FXML
    public void transformImage(ActionEvent actionEvent) {

    }

    @FXML
    public void antitransformImage(ActionEvent actionEvent) {

    }

    public void setStageWindow(Stage stageWindow) {
        this.stageWindow = stageWindow;
    }

    private void isStageWindow(){
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