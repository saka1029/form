package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TermImpl implements Term {

    public final Rational coefficient;
    public final Set<Factor> numerators;
    public final Set<Factor> denominators;

    private TermImpl(Rational coefficient, Set<Factor> numerators, Set<Factor> denominators) {
        this.coefficient = coefficient;
        this.numerators = numerators;
        this.denominators = denominators;
    }

    static Term of(Rational coefficient, Set<Factor> numerators, Set<Factor> denominators) {
        return new TermImpl(coefficient, numerators, denominators);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        return coefficient
            .multiply(numerators.stream()
                .map(x -> x.value(context))
                .reduce(Rational.ONE, (a, b) -> a.multiply(b)))
            .divide(denominators.stream()
                .map(x -> x.value(context))
                .reduce(Rational.ONE, (a, b) -> a.multiply(b)));
    }

    @Override
    public String toLisp() {
        return "(* %s (* %s) (* %s))".formatted(
            coefficient,
            numerators.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" ")),
            denominators.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" ")));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TermImpl t
            && t.coefficient.equals(coefficient)
            && t.numerators.equals(numerators)
            && t.denominators.equals(denominators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerators, denominators);
    }

    @Override
    public String toString() {
        return "Term(%s, %s, %s)".formatted(
            coefficient, numerators, denominators);
    }
}
