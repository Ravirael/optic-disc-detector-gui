/**
 * Created by rames on 05.04.17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class OpticDiscDetectorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getResource("gui" + File.separator + "MainWindow.fxml"));
        final Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
