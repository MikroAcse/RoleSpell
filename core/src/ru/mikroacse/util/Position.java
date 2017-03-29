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

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Position position) {
        set(position.x, position.y);
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void limit(int minX, int maxX, int minY, int maxY) {
        x = Math.max(x, minX);
        y = Math.max(y, minY);

        x = Math.min(x, maxX);
        y = Math.min(y, maxY);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Position position) {
        return equals(position.x, position.y);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public Position clone() {
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
