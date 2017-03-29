package ru.mikroacse.util;

import java.util.Random;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class Interval {
    private double min;
    private double max;
    private double value;

    public Interval(double min, double max, double value) {
        this.min = min;
        this.max = max;
        this.value = value;

        trim();
    }

    public Interval(double min, double max) {
        this(min, max, (max + min) / 2.0);
    }

    public Interval(double value) {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, value);
    }

    public void randomize() {
        value = min + Math.random() * (max - min);
    }

    protected void trim() {
        if(min == max) {
            value = min;
            return;
        }

        if(min > max) {
            double temp = min;
            max = min;
            min = temp;
        }

        value = Math.max(value, min);
        value = Math.min(value, max);
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
        trim();
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
        trim();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        trim();
    }

    @Override
    public String toString() {
        return "Interval{" +
                "min=" + min +
                ", max=" + max +
                ", value=" + value +
                '}';
    }
}
