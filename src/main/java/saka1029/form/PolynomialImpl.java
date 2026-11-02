package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PolynomialImpl implements Polynomial {

    public final Set<Term> terms;

    private PolynomialImpl(Set<Term> terms) {
        this.terms = terms;
    }

    public static Polynomial of(Set<Term> terms) {
        return new PolynomialImpl(terms);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        return terms.stream()
            .map(t -> t.value(context))
            .reduce(Rational.ZERO, (a, b) -> a.add(b));
    }

    @Override
    public String toLisp() {
        return "(+ %s)".formatted(terms.stream()
            .map(x -> x.toLisp())
            .collect(Collectors.joining(" ")));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PolynomialImpl p
            && p.terms.equals(terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }

    @Override
    public String toString() {
        return "Polynomial(%s)".formatted(
            terms.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining()));
    }
}