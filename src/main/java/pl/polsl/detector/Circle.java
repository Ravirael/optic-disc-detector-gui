package pl.polsl.detector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Circle {
    private final Point center;
    private final int radius;

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    @JsonCreator
    public static Circle create(@JsonProperty("center") Point center, @JsonProperty("radius") int radius) {
        return new Circle(center, radius);
    }
}
