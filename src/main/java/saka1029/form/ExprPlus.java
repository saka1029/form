package saka1029.form;

public class ExprPlus extends ExprBinary {
    public ExprPlus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(%s) + (%s)".formatted(left, right);
    }
}
