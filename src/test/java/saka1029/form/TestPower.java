package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.parse;

import org.junit.Test;

public class TestPower {

    @Test 
    public void testToDebugString() {
        assertEquals("Variable(a)", parse("a^1").toDebugString());
        assertEquals("Number(3)", parse("3^1").toDebugString());
        assertEquals("Number(9)", parse("3^2").toDebugString());
    }

}
