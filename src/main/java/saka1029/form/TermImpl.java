package saka1029.form;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import static saka1029.form.Common.*;

public class TermImpl implements Term {

    public final Factor numerator;
    public final Factor denominator;

    private TermImpl(Factor numerator, Factor denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    static Term of(Factor numerator, Factor denominator) {
        return new TermImpl(numerator, denominator);
    }

    public static Term of(long sign, List<Power> numerators, List<Power> denominators) {
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

    public static Entry<Factor, Long> splitCoefficient(Factor factor) {
        return switch (factor) {
            case FactorImpl f -> {
                yield f.powers.size() == 1
                    ? Map.entry(toList(f.powers).getFirst(), f.coefficient)
                    : Map.entry(FactorImpl.of(1, toList(f.powers)), f.coefficient);
            }
            default -> Map.entry(factor, 1L);
        };
    }

    public static Entry<Term, Rational> splitCoefficient(Term term) {
        return switch (term) {
            case FactorImpl f -> {
                Entry<Factor, Long> ff = splitCoefficient(f);
                yield Map.entry(ff.getKey(), Rational.of(ff.getValue(), 1));
            }
            case TermImpl t -> {
                Entry<Factor, Long> num = splitCoefficient(t.numerator);
                Entry<Factor, Long> den = splitCoefficient(t.denominator);
                yield Map.entry(TermImpl.of(num.getKey(), den.getKey()),
                    Rational.of(num.getValue(), den.getValue()));
            }
            default -> Map.entry(term, Rational.ONE);
        };
    }

    @Override
    public String toString() {
        return "Term(%s, %s)".formatted( numerator, denominator);
    }

    @Override
    public long value(Map<Variable, Long> context) {
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
