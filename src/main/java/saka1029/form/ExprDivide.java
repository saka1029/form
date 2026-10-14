package saka1029.form;

public class ExprDivide extends ExprBinary {
    public ExprDivide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(%s) / (%s)".formatted(left, right);
    }
}
