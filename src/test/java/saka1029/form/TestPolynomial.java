package saka1029.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static saka1029.form.Parser.*;

import java.util.Map;

import org.junit.Test;

public class TestPolynomial {

    @Test 
    public void testEquals() {
        assertEquals(parse("1"), parse("1"));
        assertEquals(parse("-1"), parse("-1"));
        assertEquals(parse("a"), parse("a"));
        assertEquals(parse("a+b"), parse("a+b"));
        assertEquals(parse("a/b"), parse("a/b"));
        assertEquals(parse("a+b+a"), parse("a+b+a"));
        assertEquals(parse("a+b+a"), parse("a+a+b"));
        assertEquals(parse("a+3b"), parse("a+3b"));
        assertEquals(parse("a-3b"), parse("-3b+a"));
        assertEquals(parse("-a-3b"), parse("-a-3b"));
        assertEquals(parse("a^2+b"), parse("a^2+b"));
        assertEquals(parse("a^2+b+a^3"), parse("a^2+b+a^3"));
        assertEquals(parse("a^2+b+a^2-c"), parse("a^2+b+a^2-c"));
        assertEquals(parse("a"), parse("(a)"));
        assertEquals(parse("aa"), parse("a(a)"));
        assertEquals(parse("aa"), parse("a*a"));
        assertEquals(parse("2aa3"), parse("2a*3a"));
        assertEquals(parse("6aa"), parse("2a*3a"));
        assertEquals(parse("a(a+1)"), parse("a(a+1)"));
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(parse("a/b"), parse("a/c"));
        assertNotEquals(parse("c/b"), parse("a/b"));
        assertNotEquals(parse("a/b"), parse("a"));
        assertNotEquals(parse("a^3"), parse("a^2"));
        assertNotEquals(parse("x^2"), parse("a^2"));
        assertNotEquals(parse("a(a+2)"), parse("a(a+1)"));
    }

    @Test 
    public void testToString() {
        assertEquals("Variable(a)", parse("a").toString());
        assertEquals("Variable(a)", parse("((a))").toString());
        assertEquals("Factor(1, [Variable(a), Variable(b)])", parse("ab").toString());
        assertEquals("Factor(1, [Power(2, Variable(a)), Variable(b)])", parse("aba").toString());
        assertEquals("Power(2, Variable(a))", parse("a^2").toString());
        assertEquals("Factor(1, [Power(2, Variable(a)), Power(3, Variable(b))])", parse("a^2b^3").toString());
        assertEquals("Variable(a)", parse("a^1").toString());
        assertEquals("Variable(a)", parse("(a)").toString());
        assertEquals("Factor(1, [Variable(a), Variable(b)])", parse("(a)b").toString());
        assertEquals("Power(2, Paren(Factor(1, [Variable(a), Variable(b)])))", parse("(ab)^2").toString());
        assertEquals("Term(Variable(a), Variable(b))", parse("a/b").toString());
        assertEquals("Term(Variable(a), Factor(3, [Variable(b)]))", parse("a/3b").toString());
        assertEquals("Polynomial([Factor(-1, [Variable(a)]), Variable(b)])", parse("-a + b").toString());
        assertEquals("Polynomial([Variable(a), Variable(a)])", parse("a + a").toString());
        assertEquals("Polynomial([Power(2, Variable(x)), Number(1)])", parse("x^2 + 1").toString());
        assertEquals("Factor(-3, [Paren(Polynomial([Variable(x), Number(1)]))])", parse("-3(x + 1)").toString());
        assertEquals("Factor(1, [Power(2, Paren(Polynomial([Variable(x), Number(1)])))])", parse("(x + 1)(x + 1)").toString());
        assertEquals("Polynomial([Variable(x), Variable(x), Number(1)])", parse("x + 1 + x").toString());
    }

    @Test
    public void testFactorToString() {
        assertEquals("Number(8)", parse("2^3").toString());
        assertEquals("Factor(6, [Variable(a)])", parse("2a3").toString());
        assertEquals("Factor(9, [Power(6, Variable(a))])", parse("a^3a^2*3^2a").toString());
    }

    @Test
    public void testFactorParenToString() {
        assertEquals("Factor(2, [Power(2, Paren(Polynomial([Variable(x), Number(1)])))])", parse("2(x+1)(x+1)").toString());
        assertEquals("Factor(2, [Power(3, Paren(Polynomial([Variable(x), Number(1)])))])", parse("2(x+1)(x+1)^2").toString());
    }

    @Test
    public void testFactorToLisp() {
        assertEquals("8", parse("2^3").toLisp());
        assertEquals("(* 6 a)", parse("2a3").toLisp());
        assertEquals("(* 9 (^ a 6))", parse("a^3 a^2 3^2 a").toLisp());
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
        assertEquals(5, parse("a + b").value(context));
        assertEquals(1, parse("-a + b").value(context));
        assertEquals(-5, parse("-a - b").value(context));
        assertEquals(-14, parse("-a - bc").value(context));
        assertEquals(25, parse("(a + b)(a+b)").value(context));
        assertEquals(55, parse("(a + b^2)(a+b)").value(context));
        assertEquals(0, parse("a / 3").value(context));
        try {
            parse("x").value(context);
            fail();
        } catch (FormException x) {
            assertEquals("variable 'x' not define", x.getMessage());
        }
    }

    @Test 
    public void testToLisp() {
        assertEquals("a", parse("a").toLisp());
        assertEquals("(* a (+ a b))", parse("a(a+b)").toLisp());
        assertEquals("(+ (^ x 2) 3)", parse("x^2+3").toLisp());
        assertEquals("(* -1 a b)", parse("-ab").toLisp());
        assertEquals("(+ (/ (* -1 a) (* 3 b)))", parse("-a/3b").toLisp());
    }

}
