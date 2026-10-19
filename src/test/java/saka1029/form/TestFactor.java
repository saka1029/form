package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.parse;

import org.junit.Test;

public class TestFactor {

    @Test 
    public void testToDebugString() {
        assertEquals("Variable(a)", parse("a").toDebugString());
        assertEquals("Paren(Paren(Variable(a)))", parse("((a))").toDebugString());
        assertEquals("Term(1, [Variable(a), Variable(b)])", parse("ab").toDebugString());
        assertEquals("Factor(*, 2, Variable(a))", parse("a^2").toDebugString());
        assertEquals("Term(1, [Factor(*, 2, Variable(a)), Factor(*, 3, Variable(b))])", parse("a^2b^3").toDebugString());
        assertEquals("Variable(a)", parse("a^1").toDebugString());
        assertEquals("Paren(Variable(a))", parse("(a)").toDebugString());
        assertEquals("Term(1, [Paren(Variable(a)), Variable(b)])", parse("(a)b").toDebugString());
    }
    
    @Test 
    public void testToString() {
        assertEquals("a", parse("a").toString());
        assertEquals("ab", parse("ab").toString());
        assertEquals("ab", parse("a*b").toString());
        assertEquals("a^2", parse("a^2").toString());
        assertEquals("a^2b^3", parse("a^2b^3").toString());
        assertEquals("a", parse("a^1").toString());
        assertEquals("(a)", parse("(a)").toString());
        assertEquals("(a)b", parse("(a)b").toString());
    }
}
