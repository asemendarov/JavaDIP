package ExampleOpenCV;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.opencv.core.Core;

public class Main extends Application {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Window/window.fxml"));
        primaryStage.setTitle("Hello World");

        BorderPane rootElement = loader.load();

        /*Scene scene = new new Scene(rootElement);
        scene.getStylesheets().add(getClass().getResource("Window/application.css").toExternalForm());
        primaryStage.setScene(scene);*/

        // or

        primaryStage.setScene(new Scene(rootElement){{
            getStylesheets().add(getClass().getResource("Window/application.css").toExternalForm());
        }});

        primaryStage.show();
    }
}