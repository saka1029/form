package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static saka1029.form.Common.*;

public class PolynomialImpl implements Polynomial {

    public final Map<Term, Integer> terms;

    public PolynomialImpl(List<Term> terms) {
        this.terms = enMap(terms);
    }

    @Override
    public String toDebugString() {
        return "Polynomial(%s)".formatted(
            toDebugStringCollection(terms));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return unMap(terms).stream()
            .mapToInt(t -> t.value(context))
            .sum();
    }

    @Override
    public String lisp() {
        return "(+ %s)".formatted(lispAll(terms));
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
        String s = unMap(terms).stream()
            .map(t -> t.toString())
            .map(t -> t.startsWith("-") ? t : "+" + t)
            .collect(Collectors.joining());
        return s.replaceFirst("^\\+", "");
    }
}