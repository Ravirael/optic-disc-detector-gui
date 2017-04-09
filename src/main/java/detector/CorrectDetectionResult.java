package detector;

import java.awt.*;
import java.util.Optional;

/**
 * Created by rames on 05.04.17.
 */
public class CorrectDetectionResult implements DetectionResult {
    private final Image image;

    public CorrectDetectionResult(Image image) {
        this.image = image;
    }

    @Override
    public Optional<Image> image() {
        return Optional.of(image);
    }

    @Override
    public Optional<String> error() {
        return Optional.empty();
    }
}
