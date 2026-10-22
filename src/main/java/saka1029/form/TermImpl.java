package saka1029.form;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TermImpl implements Term {

    public final List<Factor> factors;

    public TermImpl(LinkedList<Factor> factors) {
        this.factors = factors;
    }

    @Override
    public String toDebugString() {
        return "Term(%s)".formatted(
            Polynomial.toDebugString(factors));
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
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TermImpl t
            && t.factors.equals(factors);
    }

    @Override
    public int hashCode() {
        return factors.hashCode();
    }

    @Override
    public String toString() {
        return factors.stream()
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
