package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import static saka1029.form.Parser.*;

public class TestValue {

    @Test 
    public void testValue() {
        Map<Variable, Long> context = Map.of(
            Variable.of("a"), 2L,
            Variable.of("b"), 3L,
            Variable.of("c"), 4L
        );
        assertEquals(2, parse("a").value(context));
        assertEquals(6, parse("ab").value(context));
        assertEquals(12, parse("a^2b").value(context));
        assertEquals(108, parse("a^2b^3").value(context));
    }

}
