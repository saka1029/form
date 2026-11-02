package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TermImpl implements Term {

    public final Rational coefficient;
    public final Set<Factor> factors;

    private TermImpl(Rational coefficient, Set<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    static Term of(Rational coefficient, Set<Factor> factors) {
        return new TermImpl(coefficient, factors);
    }

    @Override
    public String toString() {
        return "Term(%s, %s)".formatted(coefficient, factors);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        return factors.stream()
            .map(x -> x.value(context))
            .reduce(Rational.ONE, (a, b) -> a.multiply(b));
    }

    @Override
    public String toLisp() {
        return "(* %s%s)".formatted(
            coefficient.equals(Rational.ONE)
                ? ""
                : coefficient + " ",
            factors.stream()
            .map(x -> x.toString())
            .collect(Collectors.joining(" ")));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TermImpl t
            && t.coefficient.equals(coefficient)
            && t.factors.equals(factors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factors);
    }
}
