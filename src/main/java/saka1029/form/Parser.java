package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * expression = expr-mult { ( '+' | '-' ) expr-mult }
 * expr-mult  = polynomial { ( '*' | '/' ) polynomial }
 * polynomial = term { ( '+' | '-' ) term }
 * term       = [ int ] factor { '*' factor }
 * factor     = primary [ '^' int ]
 * primary    = variable | '(' polynomial ')'
 */
public class Parser {

    static class CodePointBuffer {
        int[] buffer;
        int next = 0;

        public CodePointBuffer() { this.buffer = new int[64]; }

        public void append(int cp) {
            if (next >= buffer.length)
                buffer = Arrays.copyOf(buffer, buffer.length * 2);
            buffer[next++] = cp;
        }

        int pop() { return buffer[--next]; }

        public void clear() { next = 0; }

        public void clearButLast() {
            int last = pop();
            next = 0;
            append(last);
        }

        public String stringButLast() {
            int last = pop();
            String s = new String(buffer, 0, next);
            next = 0;
            append(last);
            return s;
        }
    }

    final CodePointBuffer buffer = new CodePointBuffer();
    final Reader reader;
    int ch;

    public Parser(Reader reader) {
        this.reader = reader;
        get();
    }
    public Parser(String source) {
        this(new StringReader(source));
        buffer.clearButLast();
    }

    int readCodePoint() throws IOException {
        int hi = reader.read();
        if (hi == -1)
            return -1;
        if (!Character.isHighSurrogate((char)hi))
            return hi;
        int lo = reader.read();
        if (lo == -1)
            return -1;
        if (!Character.isLowSurrogate((char)lo))
            throw new FormException("Parser.readCodePoint(): invalid surrogate pair");
        return Character.toCodePoint((char)hi, (char)lo);
    }

    int get() {
        try {
            ch = readCodePoint();
            buffer.append(ch);
            return ch;
        } catch (IOException e) {
            throw new FormException(e);
        }
    }

    void spaces() {
        while (Character.isWhitespace(ch))
            get();
    }

    boolean eat(int test) {
        spaces();
        if (ch == test) {
            get();
            return true;
        }
        return false;
    }

    static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    static boolean isVariable(int ch) {
        return Character.isAlphabetic(ch);
    }

    int readNumber() {
        while (isDigit(ch))
            get();
        return Integer.parseInt(buffer.stringButLast());
    }

    Primary readPrimary() {
        if (isVariable(ch)) {
            get();
            return new Variable(buffer.stringButLast());
        } else if (eat('(')) {
            Primary result = readPolynomial();
            if (!eat(')'))
                throw new FormException(("')' expected"));
            return result;
        } else
            throw new FormException(("variable or '(' expected"));
    }

    Factor readFactor() {
        spaces();
        int power = 1;
        Primary primary = readPrimary();
        if (eat('^')) {
            spaces();
            if (!isDigit(ch))
                throw new FormException("digit expected");
            power = readNumber();
        }
        return new Factor(power, primary);
    }

    Term readTerm(int k) {
        spaces();
        if (isDigit(ch))
            k *= readNumber();
        Term result = new Term(k);
        result.factors.add(readFactor());
        while (true)
            if (eat('*'))
                result.factors.add(readFactor());
            else
                break;
        return result;
    }

    Polynomial readPolynomial() {
        spaces();
        Polynomial result = new Polynomial();
        int sign = 1;
        if (eat('-'))
            sign = -1;
        result.terms.add(readTerm(sign));
        while (true)
            if (eat('+'))
                result.terms.add(readTerm(1));
            else if (eat('-'))
                result.terms.add(readTerm(-1));
            else
                break;
        return result;
    }

    Expression readExprMult() {
        spaces();
        Expression result = readPolynomial();
        while (true)
            if (eat('*'))
                result = new ExprMult(result, readPolynomial());
            else if (eat('/'))
                result = new ExprDivide(result, readPolynomial());
            else
                break;
        return result;
        
    }

    public Expression read() {
        spaces();
        Expression result = readExprMult();
        while (true)
            if (eat('+'))
                result = new ExprPlus(result, readExprMult());
            else if (eat('-'))
                result = new ExprMinus(result, readExprMult());
            else
                break;
        return result;
    }



}
