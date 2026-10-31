package saka1029.form;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import static saka1029.form.Parser.parse;

public class TestTermImpl {

    @Test 
    public void testSplitCoefficient() {
        assertEquals(Map.entry(parse("x"), 1L), TermImpl.splitCoefficient((Factor)parse("x")));
        assertEquals(Map.entry(parse("x"), 3L), TermImpl.splitCoefficient((Factor)parse("3x")));
        assertEquals(Map.entry(parse("xy"), 3L), TermImpl.splitCoefficient((Factor)parse("3xy")));
        assertEquals(Map.entry(parse("x^2y"), 3L), TermImpl.splitCoefficient((Factor)parse("3x^2y")));
    }

}
