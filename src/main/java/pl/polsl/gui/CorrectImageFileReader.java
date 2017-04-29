package pl.polsl.gui;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CorrectImageFileReader implements ImageFileReader {
    @Override
    public Image load(File file) throws ImageLoadingException {
        try {
            final Image image = new Image(new FileInputStream(file));

            if (image.isError()) {
                throw new ImageLoadingException("Cannot load image!", image.getException());
            }

            return image;
        } catch (FileNotFoundException e) {
            throw new ImageLoadingException("Cannot open image file " + file.getAbsolutePath() + "!", e);
        }
    }
}
