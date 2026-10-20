package saka1029.form;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FactorImpl implements Factor {

    public final FactorOperator operator;
    public final Set<Power> powers;

    public FactorImpl(FactorOperator operator, Set<Power> powers) {
        this.operator = operator;
        this.powers = powers;
    }

    @Override
    public String toDebugString() {
        return "Factor(%s, %s)".formatted(
            operator, Polynomial.toDebugString(powers));
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = 1;
        for (Power e : powers)
            result *= e.value(context);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.operator == operator
            && f.powers.equals(powers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, powers);
    }

    @Override
    public String toString() {
        return "%s%s".formatted(
            operator == FactorOperator.DIVIDE ? "/" : "",
            powers.stream().map(Object::toString).collect(Collectors.joining()));
    }
}
