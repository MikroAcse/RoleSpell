package ru.mikroacse.util;

/**
 * Created by MikroAcse on 4/6/2017.
 */
public enum Priority {
    NEVER(Integer.MIN_VALUE),
    LOWEST(-2),
    LOW(-1),
    NORMAL(0),
    HIGH(1),
    HIGHEST(2),
    IMMEDIATELY(Integer.MAX_VALUE);

    private int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int compare(Priority priority) {
        return getValue() - priority.getValue();
    }
}