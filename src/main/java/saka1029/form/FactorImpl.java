package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FactorImpl implements Factor {

    public final boolean multiply;
    public final Set<Power> powers;

    private FactorImpl(boolean multiply, Set<Power> powers) {
        this.multiply = multiply;
        this.powers = powers;
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        Rational result = Rational.ONE;
        for (Power e : powers)
            result = multiply
                ? result.multiply(e.value(context))
                : result.divide(e.value(context));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.multiply == multiply
            && f.powers.equals(powers);
    }

    /**
     * TODO: multiply == false の場合に対応していない
     */
    @Override
    public String toLisp() {
        return "(* %s)".formatted(powers.stream()
            .map(x -> x.toLisp())
            .collect(Collectors.joining(" ")));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public String toString() {
        return "Factor(%s, %s)".formatted(multiply, powers);
    }
}
