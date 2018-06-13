package ExampleOpenCV.Window;

import Tools.FXTool;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.opencv.imgcodecs.Imgcodecs;


public class Controller {
    @FXML
    public Button buttonStart;

    @FXML
    public ImageView imageView;

    @FXML
    public Label lableInf;

    @FXML
    public void run(ActionEvent actionEvent) {
        try {
            //imageView.setImage(new Image(new BufferedInputStream(new FileInputStream("resources/image/poli.jpg"))));
            /*or*/ // imageView.setImage(FXTool.mat2ImageAlternative(Imgcodecs.imread("resources/image/poli.jpg")));
            /*or*/ imageView.setImage(FXTool.mat2Image(".jpg", Imgcodecs.imread("resources/image/poli.jpg")));

        } catch (Exception ex){
            lableInf.setText(ex.getMessage());
        }
    }
}