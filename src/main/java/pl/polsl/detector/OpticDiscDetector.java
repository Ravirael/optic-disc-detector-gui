package pl.polsl.detector;

import javafx.scene.image.Image;

public interface OpticDiscDetector {
    Image detect(String filePath) throws FailedDetectionException;
}
