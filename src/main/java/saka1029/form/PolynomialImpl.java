package saka1029.form;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PolynomialImpl implements Polynomial {

    public final Set<Term> terms;

    private PolynomialImpl(Set<Term> terms) {
        this.terms = terms;
    }

    static Map<Term, Rational> toMap(List<Term> terms) {
        Map<Term, Rational> termsMap = new LinkedHashMap<>();
        for (Term term : terms) {
            switch (term) {
                case Factor f:
                    termsMap.compute(f, (k, v) -> v == null
                        ? Rational.ONE : v.add(Rational.ONE));
                    break;
                case TermImpl t:
                    Term key = TermImpl.of(Rational.ONE, t.numerators, t.denominators);
                    termsMap.compute(key, (k, v) -> v == null
                        ? Rational.ONE : v.add(t.coefficient));
                    break;
                default:
                    throw new FormException("unknown type '%s'", term);
            }
        }
        return termsMap;
    }

    static Term toTerm(Term term, Rational coefficient) {
        if (coefficient.equals(Rational.ONE))
            return term;
        else
            switch (term) {
                case Factor f:
                    if (coefficient.equals(Rational.ONE))
                        return f;
                    else
                        return TermImpl.of(coefficient, Set.of(f), Set.of());
                case TermImpl t:
                    return TermImpl.of(coefficient, t.numerators, t.denominators);
                default:
                    throw new FormException("unknown type '%s'", term);
            }
    }

    static Polynomial toPolynomial(Map<Term, Rational> termsMap) {
        if (termsMap.size() == 1) {
            Entry<Term, Rational> first = termsMap.entrySet().iterator().next();
            return toTerm(first.getKey(), first.getValue());
        } else {
            Set<Term> terms = new LinkedHashSet<>();
            for (Entry<Term, Rational> e : termsMap.entrySet())
                terms.add(toTerm(e.getKey(), e.getValue()));
            return new PolynomialImpl(terms);
        }
    }

    public static Polynomial of(List<Term> terms) {
        Map<Term, Rational> termsMap = toMap(terms);
        return toPolynomial(termsMap);
    }

    @Override
    public Rational value(Map<Variable, Rational> context) {
        return terms.stream()
            .map(t -> t.value(context))
            .reduce(Rational.ZERO, (a, b) -> a.add(b));
    }

    @Override
    public String toLisp() {
        return "(+ %s)".formatted(terms.stream()
            .map(x -> x.toLisp())
            .collect(Collectors.joining(" ")));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PolynomialImpl p
            && p.terms.equals(terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }

    @Override
    public String toString() {
        return "Polynomial(%s)".formatted(
            terms.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining()));
    }
}