package saka1029.form;

import java.util.Map;
import java.util.Objects;

public class TermImpl implements Term {

    public final Factor numerator;
    public final Factor denominator;

    public TermImpl(Factor numerator, Factor denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public String toDebugString() {
        return "Term(%s, %s)".formatted(
            numerator.toDebugString(),
            denominator.toDebugString());
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        int n = numerator.value(context);
        int d = denominator.value(context);
        return n / d;
    }

    @Override
    public String lisp() {
        return "(+ (/ %s %s))".formatted(
            numerator.lisp(), denominator.lisp());
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

    @Override
    public String toString() {
        return "%s/%s".formatted(numerator, denominator);
    }
}
