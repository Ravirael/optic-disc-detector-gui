package pl.polsl.gui;


import javafx.scene.image.Image;
import java.io.File;

interface ImageFileReader {
    Image load(File file) throws ImageLoadingException;
}
