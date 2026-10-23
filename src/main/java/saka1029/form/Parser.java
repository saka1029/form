package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

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
            int power = sign * parseNumber();
            return power == 1 ? primary
                : primary instanceof Number n ? new Number((int)Math.pow(n.value, power))
                : new PowerImpl(power, primary);
        }
        return primary;
    }

    List<Power> parseFactor(boolean positive) {
        spaces();
        LinkedList<Power> powers = new LinkedList<>();
        Power first = parsePower();
        if (!positive)
            if (first instanceof Number n)
                first = n.multiply(-1);
            else
                powers.add(Number.MINUS_ONE);
        powers.add(first);
        while (isFactor(ch))
            powers.add(parsePower());
        return powers;
    }

    Term parseTerm(boolean positive) {
        spaces();
        LinkedList<Power> numerator = new LinkedList<>();
        LinkedList<Power> denominator = new LinkedList<>();
        numerator.addAll(parseFactor(positive));
        while (true)
            if (eat('*') || isFactor(ch))
                numerator.addAll(parseFactor(true));
            else if (eat('/'))
                denominator.addAll(parseFactor(true));
            else
                break;
        Factor num = numerator.size() == 1
            ? numerator.getFirst()
            : new FactorImpl(numerator);
        Factor den = denominator.isEmpty() ? Number.ONE 
            : denominator.size() == 1 ? denominator.getFirst()
            : new FactorImpl(denominator);
        return den.equals(Number.ONE)
            ? num
            : new TermImpl(num, den);
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
