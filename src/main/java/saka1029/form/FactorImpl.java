package saka1029.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import static saka1029.form.Common.*;

public class FactorImpl implements Factor {

    public final int coefficient;
    public final Map<Power, Integer> powers;

    private FactorImpl(int coefficient, List<Power> powers) {
        this.coefficient = coefficient;
        this.powers = toBag(powers);
    }

    public static Factor of(int coefficient, List<Power> powers) {
        List<Power> list = new ArrayList<>();
        Map<Variable, Integer> notNumbers = new LinkedHashMap<>();
        for (Power e : powers)
            switch (e) {
                case Number n:
                    coefficient *= n.value;
                    break;
                case Variable v:
                    notNumbers.compute(v, (x, y) -> y == null ? 1 : y + 1);
                    break;
                case PowerImpl p:
                    switch (p.primary) {
                        case Variable v:
                            notNumbers.compute(v, (x, y) -> y == null ? p.power : y + p.power);
                            break;
                        default:
                            list.add(e);
                            break;
                    }
                    break;
                default:
                    list.add(e);
                    break;
            }
            for (Entry<Variable, Integer> e : notNumbers.entrySet())
                list.add(e.getValue() == 1
                    ? e.getKey()
                    : PowerImpl.of(e.getValue(), e.getKey()));
        return new FactorImpl(coefficient, list);
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int result = coefficient;
        for (Power e : toList(powers))
            result *= e.value(context);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FactorImpl f
            && f.coefficient == coefficient
            && f.powers.equals(powers);
    }

    @Override
    public String toLisp() {
        return "(* %s%s)".formatted(
            coefficient == 1 ? "" : coefficient + " ",
            toLispAll(toList(powers)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public String toString() {
        return "Factor(%d, %s)".formatted(
            coefficient, toStringAll(toList(powers)));
    }
}
