package saka1029.form;

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
}
