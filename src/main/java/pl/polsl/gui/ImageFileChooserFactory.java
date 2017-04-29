package pl.polsl.gui;

import javafx.stage.FileChooser;

import java.io.File;

public class ImageFileChooserFactory implements FileChooserFactory {
    public FileChooser create() {
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Image files",
                "*.JPG", "*.jpg", "*.PNG", "*.png", "*.jpg", "*.JPG"
        );
        fileChooser.getExtensionFilters().addAll(filter);
        fileChooser.setTitle("Choose image!");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        return fileChooser;
    }
}
