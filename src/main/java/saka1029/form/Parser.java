package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.SequencedSet;

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

    static boolean isElement(int ch) {
        return isDigit(ch)
            || ch == '+' || ch == '-'
            || isVariable(ch)
            || ch == '(';
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
        if (isVariable(ch)) {
            String variable = Character.toString(ch);
            get();
            return Variable.of(variable);
        } else if (eat('(')) {
            Polynomial polynomial = read();
            if (!eat(')'))
                throw new FormException(("')' expected"));
            return new Paren(polynomial);
        } else
            return new Paren(read());
            // throw new FormException(("variable or '(' expected"));
    }

    Power parsePower() {
    }

    Factor parseFactor(FactorOperator operator) {
        spaces();
        SequencedSet<Power> powers = new LinkedHashSet<>();
        powers.add(parsePower());
        while (isElement(ch))
            powers.add(parsePower());
        return operator == FactorOperator.MULTIPLY
            ? primary
            : new FactorImpl(operator, powers);
    }

    Term parseTerm() {
        spaces();
        SequencedSet<Factor> factors = new LinkedHashSet<>();
        factors.add(parseFactor(FactorOperator.MULTIPLY));
        while (true)
            if (eat('*') || isElement(ch))
                factors.add(parseFactor(FactorOperator.MULTIPLY));
            else if (eat('/'))
                factors.add(parseFactor(FactorOperator.DIVIDE));
            else
                break;
        return factors.size() == 1
            ? factors.getFirst()
            : new TermImpl(factors);
    }

    Polynomial parsePolynomial() {
        SequencedSet<Term> terms = new LinkedHashSet<>();
        terms.add(parseTerm());
        spaces();
        while (isElement(ch))
                terms.add(parseTerm());
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
