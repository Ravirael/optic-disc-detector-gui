package gui;

import javafx.stage.FileChooser;

/**
 * Created by rames on 05.04.17.
 */
public class ImageFileChooserFactory implements FileChooserFactory {
    public FileChooser create() {
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("ImageFiles files (*.jpg)", "*.JPG", "*.jpg", "*.PNG", "*.png", "*.jpg", "*.JPG");
        fileChooser.getExtensionFilters().addAll(filter);
        fileChooser.setTitle("Choose image!");
        return fileChooser;
    }
}
