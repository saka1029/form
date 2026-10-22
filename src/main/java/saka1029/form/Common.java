package saka1029.form;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Common {

    private Common() { }

    public static <T> Map<T, Integer> newMap(List<T> list) {
        Map<T, Integer> map = new LinkedHashMap<>();
        for (T e : list)
            map.compute(e, (k, v) -> v == null ? 1 : v + 1);
        return map;
    }

    public static <T> List<T> flat(Map<T, Integer> map) {
        return map.entrySet().stream()
            .flatMap(e -> IntStream.range(0, e.getValue())
                .mapToObj(i -> e.getKey()))
            .toList();
    }

    public static <T extends Polynomial> String toDebugStringCollection(Map<T, Integer> map) {
        return flat(map).stream()
            .map(Polynomial::toDebugString)
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
