package FaceDetectionAndTracking;

import FaceDetectionAndTracking.WindowThread.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.IOException;


public class Main extends Application {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WindowThread/window.fxml"));

        BorderPane rootElement = loader.load();

        primaryStage.setScene(new Scene(rootElement){{
            getStylesheets().add(getClass().getResource("WindowThread/application.css").toExternalForm());
        }});

        // WindowThread
        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest((we -> {
            try {
                controller.stop();
            } catch (IOException e) {
                controller.setLableInf(e.getMessage());
            }
        }));

        primaryStage.show();
    }
}