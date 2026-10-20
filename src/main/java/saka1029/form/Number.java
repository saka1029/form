package saka1029.form;

import java.util.Map;

public class Number implements Primary {

    public final int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return value;
    }

    @Override
    public String toDebugString() {
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
