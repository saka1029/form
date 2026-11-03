package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * polynomial = [ '-' ] term { ( '+' | '-' ) term }
 * term       = factor { ( '/' | [ '*' ] )  factor }
 * factor     = power { power }
 * power      = primary [ '^' [ '+' | '-' ] number ]
 * primary    = variable | number | '(' polynomial ')'
 * variable   = 'a' ... 'z' | 'A' ... 'Z'
 * number     = digit { digit }
 * digit      = '0' ... '9'
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

    long parseNumber() {
        sb.setLength(0);
        while (isDigit(ch)) {
            sb.appendCodePoint(ch);
            get();
        }
        return Long.parseLong(sb.toString());
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
            throw new FormException(("variable, number or '(' expected"));
    }

    Entry<Primary, Long> parsePower() {
        spaces();
        Primary primary = parsePrimary();
        long power = 1L;
        if (eat('^')) {
            long sign = eat('-') ? -1 : eat('+') ? 1 : 1;
            power = sign * parseNumber();
        }
        return Map.entry(primary, power);
    }

    Entry<Factor, Long> parseFactor() {
        spaces();
        Map<Primary, Long> powers = new LinkedHashMap<>();
        Long coefficient = 1L;
        do {
            Entry<Primary, Long> power = parsePower();
            switch (power.getKey()) {
                case Number n:
                    coefficient *= (long) Math.pow(n.value, power.getValue());
                default:
                    powers.compute(power.getKey(),
                        (k, v) -> v == null
                            ? power.getValue()
                            : v + power.getValue());
            }
        } while (isFactor(ch));
        Set<Power> powerSet = powers.entrySet().stream()
            .map(e -> PowerImpl.of(e.getValue(), e.getKey()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return Map.entry(FactorImpl.of(powerSet), coefficient);
    }

    Term parseTerm(long sign) {
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
        return TermImpl.of(sign, numerators, denominators);
    }

    Polynomial parsePolynomial() {
        spaces();
        LinkedList<Term> terms = new LinkedList<>();
        long sign = 1;
        if (eat('-'))
            sign = -1;
        terms.add(parseTerm(sign));
        spaces();
        while (true)
            if (eat('+'))
                terms.add(parseTerm(1L));
            else if (eat('-'))
                terms.add(parseTerm(-1L));
            else
                break;
        return PolynomialImpl.of(terms);
    }

    public Polynomial read() {
        spaces();
        return parsePolynomial();
    }

    public static Polynomial parse(String input) {
        return new Parser(input).read();
    }
}
