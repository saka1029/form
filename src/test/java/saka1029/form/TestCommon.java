package saka1029.form;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Parser.*;
import static saka1029.form.Common.*;

public class TestCommon {

    @Test 
    public void testMultiply() {
        assertEquals(parse("ab"), multiply(parse("a"),parse("b")));
        assertEquals(parse("ac+ad+bc+bd"), multiply(parse("a+b"),parse("c+d")));
        System.out.println(multiply(parse("a+1"),parse("a+1")).toDebugString());
        System.out.println(parse("aa+a+a+1").toDebugString());
        assertEquals(parse("aa+a+a+1"), multiply(parse("a+1"),parse("a+1")));
    }

}
