package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.parse;

import java.util.Map;

import org.junit.Test;

public class TestFactor {

    @Test 
    public void testToDebugString() {
        assertEquals("Variable(a)", parse("a").toDebugString());
        assertEquals("Paren(Paren(Variable(a)))", parse("((a))").toDebugString());
        assertEquals("Factor(*, [Variable(a), Variable(b)])", parse("ab").toDebugString());
        assertEquals("Power(2, Variable(a))", parse("a^2").toDebugString());
        assertEquals("Factor(*, [Power(2, Variable(a)), Power(3, Variable(b))])", parse("a^2b^3").toDebugString());
        assertEquals("Variable(a)", parse("a^1").toDebugString());
        assertEquals("Paren(Variable(a))", parse("(a)").toDebugString());
        assertEquals("Factor(*, [Paren(Variable(a)), Variable(b)])", parse("(a)b").toDebugString());
        assertEquals("Power(2, Paren(Factor(*, [Variable(a), Variable(b)])))", parse("(ab)^2").toDebugString());
        assertEquals("Term([Variable(a), Factor(/, [Variable(b)])])", parse("a/b").toDebugString());
        assertEquals("Term([Variable(a), Factor(/, [3, Variable(b)])])", parse("a/3b").toDebugString());
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
        assertEquals("(ab)^2", parse("(ab)^2").toString());
        assertEquals("a/b", parse("a/b").toString());
        assertEquals("a/3b", parse("a/3b").toString());
    }
    
    @Test 
    public void testValue() {
        Map<Variable, Integer> context = Map.of(
            Variable.of("a"), 2,
            Variable.of("b"), 3,
            Variable.of("c"), 4
        );
        assertEquals(2, parse("a").value(context));
        assertEquals(6, parse("ab").value(context));
        assertEquals(6, parse("a*b").value(context));
        assertEquals(4, parse("a^2").value(context));
        assertEquals(4 * 27, parse("a^2b^3").value(context));
        assertEquals(2, parse("a^1").value(context));
        assertEquals(2, parse("(a)").value(context));
        assertEquals(6, parse("(a)b").value(context));
    }
}
