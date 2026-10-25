package saka1029.form;

import java.util.List;
import java.util.stream.Collectors;

public class Common {

    private Common() { }

    // public static <T> Map<T, Integer> enMap(List<T> list) {
    //     Map<T, Integer> map = new LinkedHashMap<>();
    //     for (T e : list)
    //         map.compute(e, (k, v) -> v == null ? 1 : v + 1);
    //     return map;
    // }

    // public static <T> LinkedList<T> unMap(Map<T, Integer> map) {
    //     return map.entrySet().stream()
    //         .flatMap(e -> IntStream.range(0, e.getValue())
    //             .mapToObj(i -> e.getKey()))
    //         .collect(Collectors.toCollection(LinkedList::new));
    // }

    public static <T extends Polynomial> String toStringAll(List<T> list) {
        return list.stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    public static <T extends Polynomial> String toLispAll(List<T> list) {
        return list.stream().map(Polynomial::toLisp).collect(Collectors.joining(" "));
    }
}
