package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TestParser {

    Expression parse(String s) {
        return new Parser(s).read();
    }

    @Test 
    public void testVariable() {
        assertEquals(
            new Polynomial(List.of(
                new Term(1, List.of(
                    new Factor(1,
                        new Variable("a")))))), parse("a"));
        assertEquals(
            new Polynomial(List.of(
                new Term(1, List.of(
                    new Factor(1,
                        new Variable("a")),
                    new Factor(1,
                        new Variable("b")))))), parse("ab"));
    }

}
