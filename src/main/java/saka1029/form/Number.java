package saka1029.form;

import java.util.Map;

public class Number implements Primary {

    public static final Number ONE = new Number(1);
    public static final Number MINUS_ONE = new Number(-1);

    public final int value;

    private Number(int value) {
        this.value = value;
    }

    public static Primary of(int value) {
        return new Number(value);
    }

    public Number multiply(int n) {
        return new Number(n * value);
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return value;
    }

    @Override
    public String toString() {
        return "Number(%d)".formatted(value);
    }

    @Override
    public String toLisp() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Number n
            && n.value == value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
