package pl.polsl.detector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @JsonCreator
    public static Point create(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        return new Point(x, y);
    }
}
