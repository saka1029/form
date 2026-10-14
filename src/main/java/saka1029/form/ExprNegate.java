package saka1029.form;

public class ExprNegate implements Expression {

    public final Expression expr;

    public ExprNegate(Expression expr) {
        this.expr = expr;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExprNegate x
            && x.expr.equals(expr);
    }

    @Override
    public String toString() {
        return "-" + expr;
    }
}
