package saka1029.form;

import java.util.Map;

public class Number implements Primary {

    public static final Number ONE = new Number(1);
    public static final Number MINUS_ONE = new Number(-1);

    public final long value;

    private Number(long value) {
        this.value = value;
    }

    public static Primary of(long value) {
        return new Number(value);
    }

    public Number multiply(long n) {
        return new Number(n * value);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        return Rational.of(value);
    }

    @Override
    public String toLisp() {
        return Long.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Number n
            && n.value == value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return "Number(%d)".formatted(value);
    }
}
