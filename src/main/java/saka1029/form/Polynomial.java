package saka1029.form;

import java.util.Map;
import java.util.stream.Collectors;
import static saka1029.form.Common.*;

public interface Polynomial {
    int value(Map<Variable, Integer> context);
    String toDebugString();
    String lisp();
    default <T extends Polynomial> String lispAll(Map<T, Integer> map) {
        return unMap(map).stream().map(Polynomial::lisp).collect(Collectors.joining(" "));
    }
}
