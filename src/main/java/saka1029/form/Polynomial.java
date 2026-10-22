package saka1029.form;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface Polynomial {
    int value(Map<Variable, Integer> context);
    String toDebugString();

    public static <T extends Polynomial> String toDebugString(Collection<T> collection) {
        return collection.stream()
            .map(Polynomial::toDebugString)
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
