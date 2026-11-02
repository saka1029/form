package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FactorImpl implements Factor {

    public final Set<Power> numerators;
    public final Set<Power> denominators;

    private FactorImpl(Set<Power> numerators, Set<Power> denominators) {
        this.numerators = numerators;
        this.denominators = denominators;
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        Rational result = Rational.ONE;
        for (Power e : numerators)
            result = result.multiply(e.value(context));
        for (Power e : denominators)
            result = result.divide(e.value(context));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.numerators.equals(numerators)
            && f.denominators.equals(denominators);
    }

    @Override
    public String toLisp() {
        return "(/ (* %s) (* %s)".formatted(
            numerators.stream().map(Polynomial::toLisp)
                .collect(Collectors.joining(" ")),
            denominators.stream().map(Polynomial::toLisp)
                .collect(Collectors.joining(" ")));
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerators, denominators);
    }

    @Override
    public String toString() {
        return "Factor(%s, %s)".formatted(numerators, denominators);
    }
}
