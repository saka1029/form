package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import static saka1029.form.Parser.*;

public class TestValue {

    @Test 
    public void testValue() {
        Map<Variable, Integer> context = Map.of(
            Variable.of("a"), 2,
            Variable.of("b"), 3,
            Variable.of("c"), 4
        );
        assertEquals(2, parse("a").value(context));
        assertEquals(6, parse("ab").value(context));
        assertEquals(12, parse("a^2b").value(context));
        assertEquals(108, parse("a^2b^3").value(context));
    }

}
