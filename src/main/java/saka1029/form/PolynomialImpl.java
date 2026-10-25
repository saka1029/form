package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static saka1029.form.Common.*;

public class PolynomialImpl implements Polynomial {

    public final List<Term> terms;

    public PolynomialImpl(List<Term> terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "Polynomial(%s)".formatted(
            toStringAll(terms));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return terms.stream()
            .mapToInt(t -> t.value(context))
            .sum();
    }

    @Override
    public String toLisp() {
        return "(+ %s)".formatted(toLispAll(terms));
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
}