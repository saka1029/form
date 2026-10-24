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
                    // case Primary r:
                    //     List<Power> pp = unMap(l.powers);
                    //     pp.add(r);
                    //     return new FactorImpl(pp);
                    case Power r:
                        List<Power> pq = unMap(l.powers);
                        pq.add(r);
                        return new FactorImpl(pq);
                    case FactorImpl r:
                        List<Power> mult = unMap(l.powers);
                        mult.addAll(unMap(r.powers));
                        return new FactorImpl(mult);
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
            case TermImpl l:
                switch (right) {
                    case Paren r: return multiply(l, r.polynomial);
                    // case Primary r:
                    //     break;
                    // case PowerImpl r:
                    //     break;
                    case Factor r: return new TermImpl(
                        (Factor)multiply(l.numerator, r), l.denominator);
                    case TermImpl r: return new TermImpl(
                        (Factor)multiply(l.numerator, r.numerator),
                        (Factor)multiply(l.denominator, r.denominator)); 
                    case PolynomialImpl r: return multiply(r, l);
                    default: throw new FormException("unknown class '%s'", right);
                }
            case PolynomialImpl l:
                switch (right) {
                    case Paren r: return multiply(left, r.polynomial);
                    case Primary r: return new PolynomialImpl(unMap(l.terms).stream()
                        .map(t -> (Term)multiply(t, r)).toList());
                    case PowerImpl r: return new PolynomialImpl(unMap(l.terms).stream()
                        .map(t -> (Term)multiply(t, r)).toList());
                    case FactorImpl r: return new PolynomialImpl(unMap(l.terms).stream()
                        .map(t -> (Term)multiply(t, r)).toList());
                    case TermImpl r: return new PolynomialImpl(unMap(l.terms).stream()
                        .map(t -> (Term)multiply(t, r)).toList());
                    case PolynomialImpl r: return new PolynomialImpl(unMap(l.terms).stream()
                        .flatMap(x -> unMap(r.terms).stream().map(y -> (Term)multiply(x, y)))
                        .toList());
                    default: throw new FormException("unknown class '%s'", right);
                }
            default: throw new FormException("unknown class '%s'", left);
        }
    }
}
