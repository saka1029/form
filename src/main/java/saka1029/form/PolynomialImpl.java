package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static saka1029.form.Common.*;

public class PolynomialImpl implements Polynomial {

    public final Map<Term, Long> terms;

    private PolynomialImpl(List<Term> terms) {
        this.terms = toBag(terms);
    }

    public static Polynomial of(List<Term> terms) {
        return terms.size() == 1
            ? terms.getFirst()
            : new PolynomialImpl(terms);
    }

    @Override
    public long value(Map<Variable, Long> context) {
        return toList(terms).stream()
            .mapToLong(t -> t.value(context))
            .sum();
    }

    @Override
    public String toLisp() {
        return "(+ %s)".formatted(toLispAll(toList(terms)));
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
            toStringAll(toList(terms)));
    }
}