package pl.polsl.detector;

import javafx.scene.image.Image;

public interface OpticDiscDetector {
    DetectionResult detect(String filePath) throws FailedDetectionException;
}
