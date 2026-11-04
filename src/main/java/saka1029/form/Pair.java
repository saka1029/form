package saka1029.form;

import java.util.Objects;

public class Pair<A, B> {

    public final A a;
    public final B b;

    private Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair pair
            && pair.a.equals(a)
            && pair.b.equals(b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Pair(%s, %s)".formatted(a, b);
    }
}
