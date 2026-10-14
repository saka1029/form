package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * expression = expr-mult { ( '+' | '-' ) expr-mult }
 * expr-mult  = polynomial { ( '*' | '/' ) polynomial }
 * polynomial = term { ( '+' | '-' ) term }
 * term       = [ int ] factor { '*' factor }
 * factor     = primary [ '^' int ]
 * primary    = variable | '(' polynomial ')'
 */
public class Parser {

    final Reader reader;
    final StringBuilder sb = new StringBuilder();
    int ch;

    public Parser(Reader reader) {
        this.reader = reader;
        get();
    }
    public Parser(String source) {
        this(new StringReader(source));
        // buffer.clearButLast();
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
        sb.setLength(0);
        while (isDigit(ch)) {
            sb.appendCodePoint(ch);
            get();
        }
        return Integer.parseInt(sb.toString());
    }

    Primary readPrimary() {
        if (isVariable(ch)) {
            String variable = Character.toString(ch);
            get();
            return new Variable(variable);
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
        while (isVariable(ch) || ch == '(')
            result.factors.add(readFactor());
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
            if (isVariable(ch) || isDigit(ch) || ch == ')')
                result = new ExprMult(result, readPolynomial());
            else if (eat('/'))
                result = new ExprDivide(result, readPolynomial());
            else
                break;
        return result;
        
    }

    public Expression read() {
        spaces();
        boolean negate = false;
        if (eat('-'))
            negate = true;
        Expression result = readExprMult();
        if (negate)
            result = new ExprNegate(result);
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
