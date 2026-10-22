package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static saka1029.form.Common.*;

public class TermImpl implements Term {

    public final Map<Factor, Integer> factors;

    public TermImpl(List<Factor> factors) {
        this.factors = enMap(factors);
    }

    @Override
    public String toDebugString() {
        return "Term(%s)".formatted(
            toDebugStringCollection(factors));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = 1;
        for (Factor f : unMap(factors))
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
        return unMap(factors).stream()
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
