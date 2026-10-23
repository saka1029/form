package saka1029.form;

import java.util.Map;

public class Number implements Primary {

    public final int value;

    public Number(int value) {
        this.value = value;
    }

    public Number multiply(int n) {
        return new Number(n * value);
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return value;
    }

    @Override
    public String toDebugString() {
        return "Number(%d)".formatted(value);
    }

    @Override
    public String lisp() {
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

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
