package detector;

import java.awt.*;
import java.util.Optional;

/**
 * Created by rames on 05.04.17.
 */
public class InvalidDetectionResult implements DetectionResult {
    private final String error;

    public InvalidDetectionResult(String error) {
        this.error = error;
    }

    @Override
    public Optional<Image> image() {
        return Optional.empty();
    }

    @Override
    public Optional<String> error() {
        return Optional.of(error);
    }
}
