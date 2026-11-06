package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

    static <T> void addLong(Map<T, Long> map, Entry<T, Long> entry) {
        map.compute(entry.getKey(),
            (k, v) -> v == null ? entry.getValue() : v + entry.getValue());
    }

    static <T> void addRational(Map<T, Rational> map, Entry<T, Rational> entry) {
        map.compute(entry.getKey(),
            (k, v) -> v == null ? entry.getValue() : v.add(entry.getValue()));
    }

    static <T> void multiply(Map<T, Long> map, Entry<T, Long> entry) {
        map.compute(entry.getKey(),
            (k, v) -> v == null ? entry.getValue() : v * entry.getValue());
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
                    break;
                default:
                    addLong(powers, power);
                    break;
            }
        } while (isFactor(ch));
        LinkedHashSet<Power> powerSet = powers.entrySet().stream()
            .map(e -> PowerImpl.of(e.getValue(), e.getKey()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return powerSet.size() == 1
            ? Map.entry(powerSet.getFirst(), coefficient)
            : Map.entry(FactorImpl.of(powerSet), coefficient);
    }

    Entry<Entry<Set<Factor>, Set<Factor>>, Rational> parseTerm(long sign) {
        spaces();
        Map<Factor, Long> numerators = new LinkedHashMap<>();
        Map<Factor, Long> denominators = new LinkedHashMap<>();
        addLong(numerators, parseFactor());
        while (true)
            if (eat('*') || isFactor(ch))
                addLong(numerators, parseFactor());
            else if (eat('/'))
                addLong(denominators, parseFactor());
            else
                break;
        Rational coefficient = Rational.of(sign *
            numerators.values().stream().reduce(1L, (a, b) -> a * b),
            denominators.values().stream().reduce(1L, (a, b) -> a * b));
        System.out.printf("num=%s den=%s coef=%s%n", numerators, denominators, coefficient);
        return Map.entry(Map.entry(numerators.keySet(), denominators.keySet()), coefficient);
    }

    Polynomial parsePolynomial() {
        spaces();
        LinkedHashMap<Entry<Set<Factor>, Set<Factor>>, Rational> terms = new LinkedHashMap<>();
        long sign = 1;
        if (eat('-'))
            sign = -1;
        addRational(terms, parseTerm(sign));
        spaces();
        while (true)
            if (eat('+'))
                addRational(terms, parseTerm(1L));
            else if (eat('-'))
                addRational(terms, parseTerm(-1L));
            else
                break;
        if (terms.size() == 1) {
            var first = terms.entrySet().iterator().next();
            if (first.getKey().getKey().size() == 1 && first.getKey().getValue().isEmpty())
                return first.getKey().getKey().iterator().next();
            else
                return TermImpl.of(first.getValue(), first.getKey().getKey(), first.getKey().getValue());
        } else
            return PolynomialImpl.of(terms.entrySet().stream()
                .map(x -> TermImpl.of(x.getValue(), x.getKey().getKey(), x.getKey().getValue()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public Polynomial read() {
        spaces();
        return parsePolynomial();
    }

    public static Polynomial parse(String input) {
        return new Parser(input).read();
    }
}
