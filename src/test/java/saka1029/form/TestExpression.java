package saka1029.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestExpression {

    Expression parse(String s) {
        return new Parser(s).read();
    }

    @Test 
    public void testExpandConstant() {
        assertEquals(parse("3"), parse("1+2").expand());
        assertEquals(parse("1"), parse("-1+2").expand());
    }

    @Test
    public void testExpandFactor() {
        assertEquals(parse("x^2+3x"), parse("x^2+2x+x").expand());
        assertEquals(parse("4x^2+3x"), parse("x^2+2x+3x^2+x").expand());
        assertEquals(parse("4x^2+2x-2y"), parse("x^2+2x+3x^2-2y").expand());
        assertEquals(parse("x^2y^3+2x^2y^2"), parse("x^2y^3+2x^2y^2").expand());
    }

}
