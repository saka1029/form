package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import static saka1029.form.Parser.parse;

public class TestTermImpl {

    @Test 
    public void testSplitCoefficientFactor() {
        assertEquals(Map.entry(parse("x"), 1L), TermImpl.splitCoefficient((Factor)parse("x")));
        assertEquals(Map.entry(parse("x"), 3L), TermImpl.splitCoefficient((Factor)parse("3x")));
        assertEquals(Map.entry(parse("xy"), 3L), TermImpl.splitCoefficient((Factor)parse("3xy")));
        assertEquals(Map.entry(parse("x^2y"), 3L), TermImpl.splitCoefficient((Factor)parse("3x^2y")));
    }

    @Test 
    public void testSplitCoefficientTerm() {
        assertEquals(Map.entry(parse("x"), Rational.ONE), TermImpl.splitCoefficient((Term)parse("x")));
        assertEquals(Map.entry(parse("x"), Rational.of(3)), TermImpl.splitCoefficient((Term)parse("3x")));
        assertEquals(Map.entry(parse("x/y"), Rational.of(3, 2)), TermImpl.splitCoefficient((Term)parse("3x/2y")));
        assertEquals(Map.entry(parse("x/y"), Rational.of(3, 2)), TermImpl.splitCoefficient((Term)parse("6x/4y")));
    }

}
