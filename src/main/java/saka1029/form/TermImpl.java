package saka1029.form;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TermImpl implements Term {

    public final int coefficient;
    public final Set<Factor> factors;

    public TermImpl(int coefficient, Set<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    public TermImpl(int coefficient) {
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
            if (f instanceof FactorImpl fi)
                if (fi.operator == FactorOperator.MULTIPLY)
                    result *= f.value(context);
                else
                    result /= f.value(context);
            else
                result *= f.value(context);
        return coefficient * result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TermImpl t
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
