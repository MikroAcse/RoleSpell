package ru.mikroacse.util;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0, 0);
    }

    public static Position at(int x, int y) {
        return new Position(x, y);
    }

    public static double distance(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;

        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public static double distance(Position position1, Position position2) {
        return distance(position1.x, position1.y, position2.x, position2.y);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position position) {
        set(position.x, position.y);
    }

    public void translate(Position position) {
        translate(position.x, position.y);
    }

    public void translate(int d) {
        translate(d, d);
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void multiply(Position position) {
        multiply(position.x, position.y);
    }

    public void multiply(double d) {
        multiply(d, d);
    }

    public void multiply(double dx, double dy) {
        x *= dx;
        y *= dy;
    }

    public double distance(Position position) {
        return distance(this, position);
    }

    public double distance(int x, int y) {
        return distance(this.x, this.y, x, y);
    }

    public void limit(int minX, int maxX, int minY, int maxY) {
        x = Math.max(x, minX);
        y = Math.max(y, minY);

        x = Math.min(x, maxX);
        y = Math.min(y, maxY);
    }

    public void shorten(Position origin, int distance) {
        if (this.distance(origin) <= distance) {
            return;
        }

        double x2 = this.x - origin.x;
        double y2 = this.y - origin.y;
        double angle = Math.atan2(y2, x2);

        double x1 = Math.cos(angle) * distance;
        double y1 = Math.sin(angle) * distance;

        this.translate((int) (x1 - x2), (int) (y1 - y2));
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Position position) {
        return equals(position.x, position.y);
    }

    public Position copy() {
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
