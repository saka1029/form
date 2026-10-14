package saka1029.form;

public class ExprMinus extends ExprBinary {
    public ExprMinus(Expression left, Expression right) {
        super(left, right);
    } 

    @Override
    public String toString() {
        return "(%s) - (%s)".formatted(left, right);
    }
}
