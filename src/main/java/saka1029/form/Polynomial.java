package saka1029.form;

import java.util.Map;

public interface Polynomial {
    long value(Map<Variable, Long> context);
    // String toDebugString();
    String toLisp();
}
