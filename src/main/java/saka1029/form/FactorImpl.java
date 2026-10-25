package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static saka1029.form.Common.*;

public class FactorImpl implements Factor {

    public final int coefficient;
    public final List<Power> powers;

    private FactorImpl(int coefficient, List<Power> powers) {
        this.coefficient = coefficient;
        this.powers = powers;
    }

    public static Factor of(int coefficient, List<Power> powers) {
        return new FactorImpl(coefficient, powers);
    }

    @Override
    public String toString() {
        return "Factor(%d, %s)".formatted(
            coefficient, toStringAll(powers));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = coefficient;
        for (Power e : powers)
            result *= e.value(context);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.coefficient == coefficient
            && f.powers.equals(powers);
    }

    @Override
    public String toLisp() {
        return "(* %s%s)".formatted(
            coefficient == 1 ? "" : coefficient + " ",
            toLispAll(powers));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }
}
