package saka1029.form;

import java.util.Collection;
import java.util.stream.Collectors;

public interface ToDebugString {
    String toDebugString();
    public static <T extends ToDebugString> String toDebugString(Collection<T> collection) {
        return collection.stream()
            .map(ToDebugString::toDebugString)
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
