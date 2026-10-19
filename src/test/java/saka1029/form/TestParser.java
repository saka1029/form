package saka1029.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestParser {

    Polynomial parse(String s) {
        return new Parser(s).read();
    }

    @Test 
    public void testVariable() {
        assertEquals("x", parse("x").toString());
        assertEquals("x^2", parse("x^2").toString());
        assertEquals("x^2y", parse("x^2y").toString());
        assertEquals("3x^2y", parse("3x^2y").toString());
        assertEquals("-3x^2y", parse("-3x^2y").toString());
    }
}
