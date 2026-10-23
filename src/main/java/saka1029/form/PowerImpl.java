package saka1029.form;

import java.util.Map;

public class PowerImpl implements Power {

    public final int power;
    public final Primary primary;

    public PowerImpl(int power, Primary primary) {
        this.power = power;
        this.primary = primary;
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return (int) Math.pow(primary.value(context), power);
    }

    @Override
    public String toDebugString() {
        return "Power(%d, %s)".formatted(power, primary.toDebugString());
    }

    @Override
    public String lisp() {
        return "(^ %s %d)".formatted(primary.lisp(), power);
    }

    @Override
    public String toString() {
        String p = primary.toString();
        if (power != 1)
            p += "^" + power;
        return p;
    }

}
