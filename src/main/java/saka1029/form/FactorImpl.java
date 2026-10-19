package saka1029.form;

import java.util.Map;
import java.util.Objects;

public class FactorImpl implements Factor {

    public final FactorOperator operator;
    public final int power;
    public final Primary primary;

    public FactorImpl(FactorOperator operator, int power, Primary primary) {
        this.operator = operator;
        this.power = power;
        this.primary = primary;
    }

    @Override
    public String toDebugString() {
        return "Factor(%s, %s, %s)".formatted(
            operator, power, primary.toDebugString());
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return (int)Math.pow(primary.value(context), power);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.operator == operator
            && f.power == power
            && f.primary.equals(primary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, power, primary);
    }

    @Override
    public String toString() {
        return "%s%s%s".formatted(
            operator == FactorOperator.DIVIDE ? "/" : "",
            primary,
            power != 1 ? "^" + power : "");
    }
}
