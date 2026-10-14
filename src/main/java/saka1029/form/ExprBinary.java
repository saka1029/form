package saka1029.form;

public abstract class ExprBinary implements Expression {

    public final Expression left,right;

    public ExprBinary(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

}
