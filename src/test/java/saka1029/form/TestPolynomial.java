package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.*;

import org.junit.Test;

public class TestPolynomial {

    @Test 
    public void testEquals() {
        assertEquals(parse("1"), parse("1"));
        assertEquals(parse("-1"), parse("-1"));
        assertEquals(parse("a"), parse("a"));
        assertEquals(parse("a+b"), parse("a+b"));
        assertEquals(parse("a+b+a"), parse("a+b+a"));
        assertEquals(parse("a+3b"), parse("a+3b"));
        assertEquals(parse("a-3b"), parse("a-3b"));
        assertEquals(parse("-a-3b"), parse("-a-3b"));
        assertEquals(parse("a^2+b"), parse("a^2+b"));
        assertEquals(parse("a^2+b+a^3"), parse("a^2+b+a^3"));
        assertEquals(parse("a^2+b+a^2-c"), parse("a^2+b+a^2-c"));
        assertEquals(parse("(a)"), parse("(a)"));
        assertEquals(parse("a(a)"), parse("a(a)"));
    }

    @Test 
    public void testToString() {
        assertEquals("1", parse("1").toString());
        assertEquals("-1", parse("-1").toString());
        assertEquals("a", parse("a").toString());
        assertEquals("a+b", parse("a+b").toString());
        assertEquals("a+a+b", parse("a+b+a").toString());
        assertEquals("a+3b", parse("a+3b").toString());
        assertEquals("Polynomial([Variable(a), Factor([Number(3), Variable(b)])])",
            parse("a+3b").toDebugString());
        assertEquals("a-3b", parse("a-3b").toString());
        assertEquals("Polynomial([Factor([Number(-1), Variable(a)]), Factor([Number(-3), Variable(b)])])",
            parse("-a-3b").toDebugString());
        assertEquals("-a-3b", parse("-a-3b").toString());
        assertEquals("a^2+b", parse("a^2+b").toString());
        assertEquals("a^2+b+a^3", parse("a^2+b+a^3").toString());
        assertEquals("a^2+a^2+b-c", parse("a^2+b+a^2-c").toString());
        assertEquals("(a)", parse("(a)").toString());
    }

}
