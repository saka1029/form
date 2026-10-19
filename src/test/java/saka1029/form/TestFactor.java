package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.parse;

import org.junit.Test;

public class TestFactor {

    @Test 
    public void testToString() {
        assertEquals("a", parse("a").toString());
        assertEquals("ab", parse("ab").toString());
        assertEquals("a^2", parse("a^2").toString());
        assertEquals("a", parse("a^1").toString());
        System.out.println(parse("(a)").toDebugString());
        assertEquals("(a)", parse("(a)").toString());
        assertEquals("(a)b", parse("(a)b").toString());
    }
}
