package saka1029.form;

import java.util.Map;
import java.util.Objects;

public class PowerImpl implements Power {

    public final long power;
    public final Primary primary;

    private PowerImpl(long power, Primary primary) {
        this.power = power;
        this.primary = primary;
    }

    public static Power of(long power, Primary primary) {
        return power == 1
            ? primary
            : primary instanceof Number n
                ? Number.of((int)Math.pow(n.value, power))
                : new PowerImpl(power, primary);
    }

    @Override
    public long value(Map<Variable, Long> context) {
        return (int) Math.pow(primary.value(context), power);
    }

    @Override
    public String toLisp() {
        return "(^ %s %d)".formatted(primary.toLisp(), power);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PowerImpl p
            && p.power == power
            && p.primary.equals(primary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(power, primary);
    }

    @Override
    public String toString() {
        return "Power(%d, %s)".formatted(power, primary);
    }
}
