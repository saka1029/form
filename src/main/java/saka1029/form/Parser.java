package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

/**
 * polynomial = terms
 * terms      = term { ( '+' | '-' ) term }
 * term       = [ int ] factor { [ '/' ] factor }
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

    static boolean isFactor(int ch) {
        return isDigit(ch) || isVariable(ch) || ch == '(';
    }

    int parseNumber() {
        sb.setLength(0);
        while (isDigit(ch)) {
            sb.appendCodePoint(ch);
            get();
        }
        return Integer.parseInt(sb.toString());
    }

    Primary parsePrimary() {
        if (eat('(')) {
            Polynomial polynomial = read();
            if (!eat(')'))
                throw new FormException(("')' expected"));
            return new Paren(polynomial);
        } else if (isVariable(ch)) {
            String name = Character.toString(ch);
            get();
            return Variable.of(name);
        } else if (isDigit(ch)) {
            return new Number(parseNumber());
        } else
            // return new Paren(read());
            throw new FormException(("variable, number or '(' expected"));
    }

    Power parsePower() {
        spaces();
        Primary primary = parsePrimary();
        if (eat('^')) {
            int sign = eat('-') ? -1 : eat('+') ? 1 : 1;
            int value = sign * parseNumber();
            return value == 1 ? primary : new PowerImpl(value, primary);
        }
        return primary;
    }

    static Number MINUS_ONE = new Number(-1);

    Factor parseFactor(boolean positive, FactorOperator operator) {
        spaces();
        LinkedList<Power> powers = new LinkedList<>();
        Power first = parsePower();
        if (!positive)
            if (first instanceof Number n)
                first = n.multiply(-1);
            else
                powers.add(MINUS_ONE);
        powers.add(first);
        while (isFactor(ch))
            powers.add(parsePower());
        return powers.size() == 1 && operator == FactorOperator.MULTIPLY
            ? first
            : new FactorImpl(operator, powers);
    }

    Term parseTerm(boolean positive) {
        spaces();
        LinkedList<Factor> factors = new LinkedList<>();
        factors.add(parseFactor(positive, FactorOperator.MULTIPLY));
        while (true)
            if (eat('*') || isFactor(ch))
                factors.add(parseFactor(positive, FactorOperator.MULTIPLY));
            else if (eat('/'))
                factors.add(parseFactor(positive, FactorOperator.DIVIDE));
            else
                break;
        return factors.size() == 1
            ? factors.getFirst()
            : new TermImpl(factors);
    }

    Polynomial parsePolynomial() {
        spaces();
        LinkedList<Term> terms = new LinkedList<>();
        boolean positive = true;;
        if (eat('-'))
            positive = false;
        terms.add(parseTerm(positive));
        spaces();
        while (true)
            if (eat('+'))
                terms.add(parseTerm(true));
            else if (eat('-'))
                terms.add(parseTerm(false));
            else
                break;
        return terms.size() == 1
            ? terms.getFirst()
            : new PolynomialImpl(terms);
    }

    public Polynomial read() {
        spaces();
        return parsePolynomial();
    }

    public static Polynomial parse(String input) {
        return new Parser(input).read();
    }
}
