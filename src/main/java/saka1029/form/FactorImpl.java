package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static saka1029.form.Common.*;

public class FactorImpl implements Factor {

    public final FactorOperator operator;
    public final Map<Power, Integer> powers;

    public FactorImpl(FactorOperator operator, List<Power> powers) {
        this.operator = operator;
        this.powers = enMap(powers);
    }

    @Override
    public String toDebugString() {
        return "Factor(%s, %s)".formatted(
            operator, toDebugStringCollection(powers));
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
            unMap(powers).stream()
                .map(Object::toString)
                .collect(Collectors.joining()));
    }
}
