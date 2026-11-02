package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FactorImpl implements Factor {

    public final Set<Power> powers;

    private FactorImpl(Set<Power> powers) {
        this.powers = powers;
    }

    public static FactorImpl of(Set<Power> powers) {
        return new FactorImpl(powers);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        Rational result = Rational.ONE;
        for (Power e : powers)
            result = result.multiply(e.value(context));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.powers.equals(powers);
    }

    @Override
    public String toLisp() {
        return "(* %s)".formatted(
            powers.stream().map(Polynomial::toLisp)
                .collect(Collectors.joining(" ")));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public String toString() {
        return "Factor(%s)".formatted(powers);
    }
}
