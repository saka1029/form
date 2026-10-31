package saka1029.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestRational {

    static Rational r(long n, long d) {
        return Rational.of(n, d);
    }

    static Rational r(long n) {
        return Rational.of(n);
    }

    static Rational r(String s) {
        return Rational.parseRational(s);
    }

    @Test 
    public void testAdd() {
        assertEquals(r(5, 6), r(1, 2).add(r(1, 3)));
        assertEquals(r(-1, 6), r(1, -2).add(r(1, 3)));
        assertEquals(r(4, 3), r(1).add(r(1, 3)));
    }

    @Test 
    public void testSubtract() {
        assertEquals(r(1, 6), r(1, 2).subtract(r(1, 3)));
        assertEquals(r(-5, 6), r(1, -2).subtract(r(1, 3)));
    }

    @Test 
    public void testMultiply() {
        assertEquals(r(1, 6), r(1, 2).multiply(r(1, 3)));
        assertEquals(r(-1, 6), r(1, -2).multiply(r(1, 3)));
    }

    @Test 
    public void testDivide() {
        assertEquals(r(3, 2), r(1, 2).divide(r(1, 3)));
        assertEquals(r(-3, 2), r(1, -2).divide(r(1, 3)));
    }

    @Test 
    public void testCompareTo() {
        assertEquals(0, r("1/2").compareTo(r("4/8")));
        assertEquals(1, r("1/2").compareTo(r("1/3")));
        assertEquals(-1, r("1/-2").compareTo(r("1/3")));
    }

    @Test 
    public void testParseRational() {
        assertEquals(r("3/2"), r("1/2").divide(r("1/3")));
        assertEquals(r("-3/2"), r("1/-2").divide(r("1/3")));
    }

}
