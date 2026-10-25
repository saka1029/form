package saka1029.form;

import java.util.Map;
import java.util.Objects;

public class Paren implements Primary {

    public final Polynomial polynomial;

    private Paren(Polynomial polynomial) {
        this.polynomial = polynomial;
    }

    public static Primary of(Polynomial polynomial) {
        return polynomial instanceof Primary primary
            ? primary
            : new Paren(polynomial);
    }

    @Override
    public String toLisp() {
        return polynomial.toLisp();
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return polynomial.value(context);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Paren p
            && p.polynomial.equals(polynomial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(polynomial);
    }

    @Override
    public String toString() {
        return "Paren(%s)".formatted(polynomial);
    }
}
