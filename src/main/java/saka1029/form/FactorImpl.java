package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import static saka1029.form.Common.*;

public class FactorImpl implements Factor {

    public final Map<Power, Integer> powers;

    public FactorImpl(List<Power> powers) {
        this.powers = enMap(powers);
        if (this.powers.size() > 1)
            this.powers.remove(Number.ONE);
        if (this.powers.containsKey(Number.ONE))
            this.powers.put(Number.ONE, 1);
    }

    @Override
    public String toDebugString() {
        return "Factor(%s)".formatted(
            toDebugStringAll(powers));
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
    public String toLisp() {
        return "(* %s)".formatted(toLispAll(powers));
    }

    @Override
    public int hashCode() {
        return Objects.hash(powers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Power e : unMap(powers))
            sb.append(i++ == 0 && e.equals(Number.MINUS_ONE)
                ? "-"
                : e.toString());
        return sb.toString();
    }
}
