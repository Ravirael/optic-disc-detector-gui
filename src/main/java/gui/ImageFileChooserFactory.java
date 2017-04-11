package gui;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by rames on 05.04.17.
 */
public class ImageFileChooserFactory implements FileChooserFactory {
    public FileChooser create() {
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ImageFiles files (*.jpg)", "*.JPG", "*.jpg", "*.PNG", "*.png", "*.jpg", "*.JPG");
        fileChooser.getExtensionFilters().addAll(filter);
        fileChooser.setTitle("Choose image!");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        return fileChooser;
    }
}
