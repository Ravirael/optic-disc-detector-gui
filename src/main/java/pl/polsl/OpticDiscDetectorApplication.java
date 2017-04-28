package pl.polsl; /**
 * Created by rames on 05.04.17.
 */

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.polsl.gui.MainWindowController;
import pl.polsl.gui.ParametersResourceBundle;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class OpticDiscDetectorApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"), new ParametersResourceBundle(getParameters()));
        final Parent root = loader.load();

        final Scene scene = new Scene(root);
        final InvalidationListener resizeListener = new InvalidationListener() {
            @Override
            public void invalidated(final Observable observable) {
                System.out.println("Window maximized");
                ((MainWindowController)loader.getController()).calibrateImage();
            }
        };
        stage.maximizedProperty().addListener(resizeListener);

        stage.setScene(scene);
        stage.show();
    }
}
