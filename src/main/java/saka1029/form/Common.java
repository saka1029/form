package saka1029.form;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Common {

    private Common() { }

    /**
     * リストをバッグ(要素の重複を許す集合)に変換する。
     * バッグの実体は要素から、その要素数へのマップである。
     */
    public static <T> Map<T, Integer> toBag(List<T> list) {
        return list.stream()
            .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.summingInt(e -> 1)));
    }

    /**
     * バッグをリストに変換する。 
     */
    public static <T> LinkedList<T> toList(Map<T, Integer> bag) {
        return bag.entrySet().stream()
            .flatMap(e -> IntStream.range(0, e.getValue())
                .mapToObj(i -> e.getKey()))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public static <T extends Polynomial> String toStringAll(List<T> list) {
        return list.stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    public static <T extends Polynomial> String toLispAll(List<T> list) {
        return list.stream().map(Polynomial::toLisp).collect(Collectors.joining(" "));
    }
}
