package ru.mikroacse.engine.utils;

/**
 * Created by MikroAcse on 28.03.2017.
 */
public class Vector2 {
    public int x;
    public int y;
    
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2() {
        this(0, 0);
    }
    
    public static Vector2 at(int x, int y) {
        return new Vector2(x, y);
    }
    
    public static double distance(int x1, int y1, int x2, int y2) {
        x1 -= x2;
        y1 -= y2;
        
        return Math.sqrt(x1 * x1 + y1 * y1);
    }
    
    public static double distance(Vector2 vector2A, Vector2 vector2B) {
        return distance(vector2A.x, vector2A.y, vector2B.x, vector2B.y);
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Vector2 vector2) {
        set(vector2.x, vector2.y);
    }
    
    public void translate(Vector2 vector2) {
        translate(vector2.x, vector2.y);
    }
    
    public void translate(int d) {
        translate(d, d);
    }
    
    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    public void multiply(Vector2 vector2) {
        multiply(vector2.x, vector2.y);
    }
    
    public void multiply(double d) {
        multiply(d, d);
    }
    
    public void multiply(double dx, double dy) {
        x *= dx;
        y *= dy;
    }
    
    public double distance(Vector2 vector2) {
        return distance(this, vector2);
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
    
    public void shorten(Vector2 origin, int distance) {
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
    
    public boolean equals(Vector2 vector2) {
        return equals(vector2.x, vector2.y);
    }
    
    public Vector2 copy() {
        return new Vector2(x, y);
    }
    
    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
