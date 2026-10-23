package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static saka1029.form.Common.*;

public class FactorImpl implements Factor {

    public final Map<Power, Integer> powers;

    public FactorImpl(List<Power> powers) {
        this.powers = enMap(powers);
    }

    @Override
    public String toDebugString() {
        return "Factor(%s)".formatted(
            toDebugStringCollection(powers));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = 1;
        for (Power e : unMap(powers))
            result *= e.value(context);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.powers.equals(powers);
    }

    @Override
    public String lisp() {
        return "(%s)".formatted(lispAll(powers));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public String toString() {
        return unMap(powers).stream()
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
