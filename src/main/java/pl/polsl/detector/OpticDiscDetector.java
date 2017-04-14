package pl.polsl.detector;

import javafx.scene.image.Image;

/**
 * Created by rames on 05.04.17.
 */
public interface OpticDiscDetector {
    Image detect(String filePath) throws FailedDetectionException;
}
