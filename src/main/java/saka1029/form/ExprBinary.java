package saka1029.form;

public abstract class ExprBinary implements Expression {

    public final Expression left,right;

    public ExprBinary(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        Class<?> cls = getClass();
        return cls.equals(obj.getClass())
            && ((ExprBinary)obj).left.equals(left)
            && ((ExprBinary)obj).right.equals(right);
    }
}
