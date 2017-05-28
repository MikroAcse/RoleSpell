package ru.mikroacse.engine.util;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class Interval {
    private double min;
    private double max;
    private double value;

    private Listener listeners;

    public Interval(double min, double max, double value) {
        this.min = min;
        this.max = max;

        listeners = ListenerSupportFactory.create(Listener.class);

        setValue(value);
    }

    public Interval(double min, double max) {
        this(min, max, min);
    }

    public Interval(double value) {
        this(Double.MIN_VALUE, Double.MAX_VALUE, value);
    }

    public Interval() {
        this(0);
    }

    public void randomize() {
        if (min != max) {
            setValue(min + Math.random() * (max - min));
        }
    }

    public double getPercentage() {
        if (min == max) {
            return 1;
        }
        if (value == min) {
            return 0;
        }

        return (value - min) / (max - min);
    }

    public void setPercentage(double percentage) {
        setValue(min + percentage * (max - min));
    }

    public void add(double value) {
        setValue(this.value + value);
    }

    public void subtract(double value) {
        setValue(this.value - value);
    }

    public void multiply(double value) {
        setValue(this.value * value);
    }

    public void divide(double value) {
        setValue(this.value / value);
    }

    public boolean isMax() {
        return value == max;
    }

    public boolean isMin() {
        return value == min;
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

    public void addListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).addListener(listener);
    }

    public void removeListener(Listener listener) {
        ((ListenerSupport<Listener>) listeners).removeListener(listener);
    }

    public void clearListeners() {
        ((ListenerSupport<Listener>) listeners).clearListeners();
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
        setValue(value);
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        double prev = this.value;

        this.value = value;

        trim();

        if (prev != this.value) {
            listeners.valueChanged(this, prev, this.value);
        }
    }

    @Override
    public String toString() {
        return "LimitedDouble{" +
                "min=" + min +
                ", max=" + max +
                ", interval=" + value +
                '}';
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void valueChanged(Interval interval, double previousValue, double currentValue);
    }
}
