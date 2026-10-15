package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class TestParser {

    Expression parse(String s) {
        return new Parser(s).read();
    }

    @Test 
    public void testVariable() {
        assertEquals(
            new Polynomial(Set.of(
                new Term(1, Set.of(
                    new Factor(1,
                        Variable.of("a")))))), parse("a"));
        assertEquals(
            new Polynomial(Set.of(
                new Term(1, Set.of(
                    new Factor(2,
                        Variable.of("a")))))), parse("a^2"));
        assertEquals(
            new Polynomial(Set.of(
                new Term(1, Set.of(
                    new Factor(3,
                        Variable.of("a")),
                    new Factor(4,
                        Variable.of("b")))))), parse("a^3b^4"));
    }

    @Test 
    public void testTerm() {
        assertEquals(
            new Polynomial(Set.of(
                new Term(5, Set.of(
                    new Factor(1,
                        Variable.of("a")))))), parse("5a"));
        assertEquals(
            new Polynomial(Set.of(
                new Term(7, Set.of()))), parse("7"));
    }

    @Test
    public void testTermMinus() {
        assertEquals(
            new Polynomial(Set.of(
                new Term(-7, Set.of()))), parse("-7"));
    }

    @Test
    public void testPolynomial() {
        assertEquals(
            new Polynomial(Set.of(
                new Term(1, Set.of(
                    new Factor(1,
                        Variable.of("a")))),
                new Term(1, Set.of(
                    new Factor(1,
                        Variable.of("b")))))), parse("a+b"));
        assertEquals(
            new Polynomial(Set.of(
                new Term(1, Set.of(
                    new Factor(1,
                        Variable.of("a")))),
                new Term(3, Set.of(
                    new Factor(1,
                        Variable.of("b")),
                    new Factor(1,
                        Variable.of("c")))))), parse("a+3bc"));
    }
}
