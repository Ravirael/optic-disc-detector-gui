package pl.polsl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.polsl.gui.MainWindowController;
import pl.polsl.gui.ParametersResourceBundle;

import java.io.IOException;

public class OpticDiscDetectorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(
                MainWindowController.class.getResource("MainWindow.fxml"),
                new ParametersResourceBundle(getParameters())
        );
        final Parent root = loader.load();

        final Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
