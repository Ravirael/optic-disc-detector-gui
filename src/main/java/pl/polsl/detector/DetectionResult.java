package pl.polsl.detector;

import javafx.scene.image.Image;

public class DetectionResult {
    private final Image image;
    private final Circle circle;

    public DetectionResult(Image image, Circle circle) {
        this.image = image;
        this.circle = circle;
    }

    public Image getImage() {
        return image;
    }

    public Circle getCircle() {
        return circle;
    }

    public static DetectionResult create(Image image, Circle circle) {
        return new DetectionResult(image, circle);
    }
}
