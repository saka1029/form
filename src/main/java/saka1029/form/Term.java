package saka1029.form;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Term {

    public final int coefficient;
    public final Set<Factor> factors;

    public Term(int coefficient, Set<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    public Term(int coefficient) {
        this(coefficient, new LinkedHashSet<>());
    }

    public Term reduce() {
        return new Term(coefficient, factors.stream()
            .map(x -> x.reduce())
            .collect(Collectors.toSet()));

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Term t
            && t.coefficient == coefficient
            && t.factors.equals(factors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficient, factors);
    }

    @Override
    public String toString() {
        String fs = factors.stream()
            .map(f -> f.toString())
            .collect(Collectors.joining());
        return coefficient == 1 ? fs : coefficient + fs;
    }
}
