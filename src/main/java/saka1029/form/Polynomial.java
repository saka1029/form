package saka1029.form;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Polynomial implements Primary {

    public final List<Term> terms;

    public Polynomial(List<Term> terms) {
        this.terms = terms;
    }

    public Polynomial() {
        this(new ArrayList<>());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Polynomial p
            && p.terms.equals(terms);
    }

    @Override
    public String toString() {
        String s = terms.stream()
            .map(t -> t.toString())
            .map(t -> t.startsWith("-") ? t : "+" + t)
            .collect(Collectors.joining());
        return s.replaceFirst("^\\+", "");
    }

}
