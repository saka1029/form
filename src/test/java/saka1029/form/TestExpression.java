package saka1029.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestExpression {

    Expression parse(String s) {
        return new Parser(s).read();
    }

    @Test 
    public void testReduceConstant() {
        assertEquals(parse("3"), parse("1+2").reduce());
        assertEquals(parse("1"), parse("-1+2").reduce());
    }

    @Test
    public void testReduceFactor() {
        assertEquals(parse("x^2+3x"), parse("x^2+2x+x").reduce());
        assertEquals(parse("4x^2+3x"), parse("x^2+2x+3x^2").reduce());
    }

}
