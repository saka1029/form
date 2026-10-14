package saka1029.form;

public class ExprMult extends ExprBinary {
    public ExprMult(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(%s) (%s)".formatted(left, right);
    }
}
