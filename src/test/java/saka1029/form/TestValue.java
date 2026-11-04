package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import static saka1029.form.Parser.*;

public class TestValue {

    @Test 
    public void testValue() {
        Map<Variable, Rational> context = Map.of(
            Variable.of("a"), Rational.of(2L),
            Variable.of("b"), Rational.of(3L),
            Variable.of("c"), Rational.of(4L)
        );
        assertEquals(Rational.of(2), parse("a").value(context));
        assertEquals(Rational.of(6), parse("ab").value(context));
        assertEquals(Rational.of(12), parse("a^2b").value(context));
        assertEquals(Rational.of(108), parse("a^2b^3").value(context));
    }

}
