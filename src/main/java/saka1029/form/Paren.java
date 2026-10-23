package saka1029.form;

import java.util.Map;

public class Paren implements Primary {

    public final Polynomial polynomial;

    public Paren(Polynomial polynomial) {
        this.polynomial = polynomial;
    }

    @Override
    public String toDebugString() {
        return "Paren(%s)".formatted(polynomial.toDebugString());
    }

    @Override
    public String lisp() {
        return "(%s)".formatted(polynomial.lisp());
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        return polynomial.value(context);
    }

    @Override
    public String toString() {
        return "(%s)".formatted(polynomial);
    }
}
