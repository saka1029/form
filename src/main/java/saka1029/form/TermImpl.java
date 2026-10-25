package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TermImpl implements Term {

    public final Factor numerator;
    public final Factor denominator;

    private TermImpl(Factor numerator, Factor denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Term of(int sign, List<Power> numerators, List<Power> denominators) {
        Factor num = numerators.size() == 1 && sign == 1
            ? numerators.getFirst()
            : FactorImpl.of(sign, numerators);
        Factor den = denominators.size() == 0
            ? Number.ONE
            : denominators.size() == 1
                ? denominators.getFirst()
                : FactorImpl.of(1, denominators);
        return den == Number.ONE ? num : new TermImpl(num, den);
    }

    @Override
    public String toString() {
        return "Term(%s, %s)".formatted( numerator, denominator);
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return numerator.value(context) / denominator.value(context);
    }

    @Override
    public String toLisp() {
        return denominator.equals(Number.ONE)
            ? "(+ %s)".formatted(numerator.toLisp())
            : "(+ (/ %s %s))".formatted(numerator.toLisp(), denominator.toLisp());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TermImpl t
            && t.numerator.equals(numerator)
            && t.denominator.equals(denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}
