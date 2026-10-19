package saka1029.form;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Terms implements Polynomial {

    public final Set<Term> terms;

    public Terms(Set<Term> terms) {
        this.terms = terms;
    }

    public Terms() {
        this(new LinkedHashSet<>());
    }

    @Override
    public String toDebugString() {
        return "Terms(%s)".formatted(ToDebugString.toDebugString(terms));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return terms.stream()
            .mapToInt(t -> t.value(context))
            .sum();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Terms t
            && t.terms.equals(terms);
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
