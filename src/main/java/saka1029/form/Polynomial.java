package saka1029.form;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Polynomial implements Primary {

    public final Set<Term> terms;

    public Polynomial(Set<Term> terms) {
        this.terms = terms;
    }

    public Polynomial() {
        this(new LinkedHashSet<>());
    }

    @Override
    public Polynomial expand() {
        Map<Set<Factor>, Integer> newTerms = new LinkedHashMap<>();
        for (Term term : terms) {
            Integer coefficient = newTerms.get(term.factors);
            if (coefficient == null)
                coefficient = 0;
            coefficient += term.coefficient;
            newTerms.put(term.factors, coefficient);
        }
        return new Polynomial(newTerms.entrySet().stream()
            .map(e -> new Term(e.getValue(), e.getKey()))
            .collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Polynomial p
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
