package saka1029.form;

import java.util.Map;

public interface Polynomial {
    Rational value(Map<Variable, Rational> context);
    String toLisp();
}
