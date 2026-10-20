package saka1029.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static saka1029.form.Parser.*;

public class TestParser {

    @Test 
    public void testIsVariable() {
        assertTrue(isVariable('a'));
        assertTrue(isVariable('あ'));
        assertTrue(isVariable('漢'));
        assertTrue(isVariable(Character.codePointAt("𩸽", 0)));
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
