package saka1029.form;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Common {

    private Common() { }

    public static <T> Map<T, Integer> enMap(List<T> list) {
        Map<T, Integer> map = new LinkedHashMap<>();
        for (T e : list)
            map.compute(e, (k, v) -> v == null ? 1 : v + 1);
        return map;
    }

    public static <T> LinkedList<T> unMap(Map<T, Integer> map) {
        return map.entrySet().stream()
            .flatMap(e -> IntStream.range(0, e.getValue())
                .mapToObj(i -> e.getKey()))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public static <T extends Polynomial> String toDebugStringAll(Map<T, Integer> map) {
        return unMap(map).stream()
            .map(Polynomial::toDebugString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    public static <T extends Polynomial> String toLispAll(Map<T, Integer> map) {
        return unMap(map).stream().map(Polynomial::toLisp).collect(Collectors.joining(" "));
    }

    public static Polynomial multiply(Polynomial left, Polynomial right) {
        switch (left) {
            case Paren l:
                return multiply(l.polynomial, right);
            case Primary l:
                switch (right) {
                    case Paren r: return multiply(l, r.polynomial);
                    case Primary r: return new FactorImpl(List.of(l, r));
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
            case PowerImpl l:
                switch (right) {
                    case Paren r: return multiply(l, r.polynomial);
                    case Primary r: return new FactorImpl(List.of(l, r));
                    case PowerImpl r: return l.primary.equals(r.primary)
                        ? new PowerImpl(l.power + r.power, l.primary)
                        : new FactorImpl(List.of(l, r));
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
            case FactorImpl l:
                switch (right) {
                    case Paren r: return multiply(l, r.polynomial);
                    case Primary r:
                        break;
                    case PowerImpl r:
                        break;
                    case FactorImpl r:
                        break;
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
                break;
            case TermImpl l:
                switch (right) {
                    case Paren r: return multiply(l, r.polynomial);
                    case Primary r:
                        break;
                    case PowerImpl r:
                        break;
                    case FactorImpl r:
                        break;
                    case TermImpl r:
                        break;
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
                break;
            case PolynomialImpl l:
                switch (right) {
                    case Paren r: return multiply(left, r.polynomial);
                    case Primary r:
                        break;
                    case PowerImpl r:
                        break;
                    case FactorImpl r:
                        break;
                    case TermImpl r:
                        break;
                    case PolynomialImpl r:
                        break;
                    default: throw new FormException("unknown class '%s'", right);
                }
                break;
            default: throw new FormException("unknown class '%s'", left);
        };
        return null;
    }
}
