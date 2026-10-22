package saka1029.form;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PolynomialImpl implements Polynomial {

    public final List<Term> terms;

    public PolynomialImpl(List<Term> terms) {
        this.terms = terms;
    }
    public PolynomialImpl() {
        this(new LinkedList<>());
    }

    @Override
    public String toDebugString() {
        return "Polynomial(%s)".formatted(
            Polynomial.toDebugString(terms));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return terms.stream()
            .mapToInt(t -> t.value(context))
            .sum();
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
        String s = terms.stream()
            .map(t -> t.toString())
            .map(t -> t.startsWith("-") ? t : "+" + t)
            .collect(Collectors.joining());
        return s.replaceFirst("^\\+", "");
    }
}