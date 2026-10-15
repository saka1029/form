package saka1029.form;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Term {

    public final int coefficient;
    public final List<Factor> factors;

    public Term(int coefficient, List<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    public Term(int coefficient) {
        this(coefficient, new ArrayList<>());
    }

    public Term reduce() {
        return new Term(coefficient, factors.stream()
            .map(x -> x.reduce())
            .toList());

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Term t
            && t.coefficient == coefficient
            && t.factors.equals(factors);
    }

    @Override
    public String toString() {
        String fs = factors.stream()
            .map(f -> f.toString())
            .collect(Collectors.joining());
        return coefficient == 1 ? fs : coefficient + fs;
    }
}
