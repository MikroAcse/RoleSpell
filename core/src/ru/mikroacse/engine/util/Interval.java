package ru.mikroacse.engine.util;

import ru.mikroacse.engine.listeners.ListenerSupport;
import ru.mikroacse.engine.listeners.ListenerSupportFactory;

import java.util.Random;

/**
 * Created by MikroAcse on 29.03.2017.
 */
public class Interval {
    private double min;
    private double max;
    private double value;

    private Listener listeners;

    private boolean randomized;

    public Interval(double min, double max, double value, boolean randomized) {
        this.min = min;
        this.max = max;
        this.randomized = randomized;

        listeners = ListenerSupportFactory.create(Listener.class);

        setValue(value);
    }

    public Interval(double min, double max, double value) {
        this(min, max, value, false);
    }

    public Interval(double min, double max) {
        this(min, max, min, true);
    }

    public Interval(double value) {
        this(Double.MIN_VALUE, Double.MAX_VALUE, value, false);
    }

    public Interval() {
        this(0);
    }

    public void randomize() {
        if (min != max) {
            setValue(min + Math.random() * (max - min));
        } else {
            setValue(min);
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
            double temp = max;
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

    public double getValue(boolean randomized) {
        if(this.randomized && randomized) {
            randomize();
        }

        return value;
    }

    public double getValue() {
        return getValue(true);
    }

    public void setValue(double value) {
        double prev = this.value;

        this.value = value;

        trim();

        if (prev != this.value) {
            listeners.valueChanged(this, prev, this.value);
        }
    }

    public boolean isRandomized() {
        return randomized;
    }

    public void setRandomized(boolean randomized) {
        this.randomized = randomized;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "min=" + min +
                ", max=" + max +
                ", value=" + value +
                '}';
    }

    public interface Listener extends ru.mikroacse.engine.listeners.Listener {
        void valueChanged(Interval interval, double previousValue, double currentValue);
    }
}
