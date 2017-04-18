package ru.mikroacse.util;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class LimitedDouble {
    private double min;
    private double max;
    private double value;

    public LimitedDouble(double min, double max, double value) {
        this.min = min;
        this.max = max;
        this.value = value;

        trim();
    }

    public LimitedDouble(double min, double max) {
        this(min, max, min);
    }

    public LimitedDouble(double value) {
        this(Double.MIN_VALUE, Double.MAX_VALUE, value);
    }

    public LimitedDouble() {
        this(0);
    }

    public void randomize() {
        if (min != max) {
            value = min + Math.random() * (max - min);
        }
    }

    public double getPercentage() {
        if (min == max) return 1;

        return (value - min) / (max - min);
    }

    public void setPercentage(double percentage) {
        value = min + percentage * (max - min);
        trim();
    }

    private void trim() {
        if (min == max) {
            value = min;
            return;
        }

        if (min > max) {
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
        return "LimitedDouble{" +
                "min=" + min +
                ", max=" + max +
                ", value=" + value +
                '}';
    }
}
