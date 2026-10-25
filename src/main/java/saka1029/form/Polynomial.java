package saka1029.form;

import java.util.Map;

public interface Polynomial {
    int value(Map<Variable, Integer> context);
    // String toDebugString();
    String toLisp();
}
