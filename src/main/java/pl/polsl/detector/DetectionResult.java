package pl.polsl.detector;

import java.awt.*;
import java.util.Optional;

/**
 * Created by rames on 05.04.17.
 */
public interface DetectionResult {
    Optional<Image> image();
    Optional<String> error();
}
