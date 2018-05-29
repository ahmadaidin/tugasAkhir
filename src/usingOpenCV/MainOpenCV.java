package usingOpenCV;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class MainOpenCV extends Application {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("openCV.fxml"));
        primaryStage.setTitle("TeA-OpenCV");
        primaryStage.setScene(new Scene(root, 750, 600));
        primaryStage.show();
    }
}
