package saka1029.form;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Term implements Value, ToDebugString {

    public final int coefficient;
    public final Set<Factor> factors;

    public Term(int coefficient, Set<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    public Term(int coefficient) {
        this(coefficient, new LinkedHashSet<>());
    }

    @Override
    public String toDebugString() {
        return "Term(%d, %s)".formatted(coefficient,
            ToDebugString.toDebugString(factors));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = 1;
        for (Factor f : factors)
            if (f.operator == FactorOperator.MULTIPLY)
                result *= f.value(context);
            else
                result /= f.value(context);
        return coefficient * result;
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
