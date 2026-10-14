package saka1029.form;

public class ExprNegate implements Expression {

    public final Expression expr;

    public ExprNegate(Expression expr) {
        this.expr = expr;
    }

}
