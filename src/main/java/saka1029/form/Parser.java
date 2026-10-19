package saka1029.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

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
            return polynomial;
        } else
            throw new FormException(("variable or '(' expected"));
    }

    Factor parseFactor(FactorOperator operator) {
        spaces();
        int power = 1;
        Primary primary = parsePrimary();
        if (eat('^')) {
            spaces();
            if (!isDigit(ch))
                throw new FormException("digit expected");
            power = parseNumber();
        }
        return new Factor(operator, power, primary);
    }

    Term parseTerm(int k) {
        spaces();
        if (isDigit(ch))
            k *= parseNumber();
        Term result = new Term(k);
        while (true)
            if (isVariable(ch) || ch == '(')
                result.factors.add(parseFactor(FactorOperator.MULTIPLY));
            else if (eat('/'))
                result.factors.add(parseFactor(FactorOperator.DIVIDE));
            else
                break;
        return result;
    }

    Terms parseTerms() {
        Terms result = new Terms();
        result.terms.add(parseTerm(eat('-') ?  -1 : 1));
        while (true)
            if (eat('+'))
                result.terms.add(parseTerm(1));
            else if (eat('-'))
                result.terms.add(parseTerm(-1));
            else
                break;
        return result;
    }

    public Polynomial read() {
        spaces();
        return parseTerms();
    }

    public static Polynomial parse(String input) {
        return new Parser(input).read();
    }
}
