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
            return Paren.of(polynomial);
        } else if (isVariable(ch)) {
            String name = Character.toString(ch);
            get();
            return Variable.of(name);
        } else if (isDigit(ch)) {
            return Number.of(parseNumber());
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
                : primary instanceof Number n ? Number.of((int)Math.pow(n.value, power))
                : new PowerImpl(power, primary);
        }
        return primary;
    }

    List<Power> parseFactor() {
        spaces();
        LinkedList<Power> powers = new LinkedList<>();
        powers.add(parsePower());
        while (isFactor(ch))
            powers.add(parsePower());
        return powers;
    }

    Term parseTerm(int sign) {
        spaces();
        LinkedList<Power> numerators = new LinkedList<>();
        LinkedList<Power> denominators = new LinkedList<>();
        numerators.addAll(parseFactor());
        while (true)
            if (eat('*') || isFactor(ch))
                numerators.addAll(parseFactor());
            else if (eat('/'))
                denominators.addAll(parseFactor());
            else
                break;
        if (sign == 1 && numerators.size() == 1
            && (denominators.size() == 0 || denominators.getFirst().equals(Number.ONE)))
            return numerators.getFirst();
        Factor num = numerators.size() == 1 && sign == 1
            ? numerators.getFirst()
            : new FactorImpl(sign, numerators);
        Factor den = denominators.size() == 0
            ? Number.ONE
            : denominators.size() == 1
                ? denominators.getFirst()
                : new FactorImpl(1, denominators);
        return den == Number.ONE ? num : new TermImpl(num, den);
    }

    Polynomial parsePolynomial() {
        spaces();
        LinkedList<Term> terms = new LinkedList<>();
        int sign = 1;
        if (eat('-'))
            sign = -1;
        terms.add(parseTerm(sign));
        spaces();
        while (true)
            if (eat('+'))
                terms.add(parseTerm(1));
            else if (eat('-'))
                terms.add(parseTerm(-1));
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
