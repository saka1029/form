package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.parse;

import java.util.Map;

import org.junit.Test;

public class TestTerm {

    @Test 
    public void testToDebugString() {
        assertEquals("Polynomial([Term(-1, [Variable(a)]), Variable(b)])", parse("-a + b").toDebugString());
    }

    @Test 
    public void testValue() {
        Map<Variable, Integer> context = Map.of(
            Variable.of("a"), 2,
            Variable.of("b"), 3,
            Variable.of("c"), 4
        );
        assertEquals(5, parse("a + b").value(context));
        assertEquals(1, parse("-a + b").value(context));
        assertEquals(-5, parse("-a - b").value(context));
        assertEquals(-14, parse("-a - bc").value(context));
        assertEquals(25, parse("(a + b)(a+b)").value(context));
    }

}
