package saka1029.form;

public class ExprBinary implements Expression {

    public enum Op {
        PLUS(" + "),
        MINUS(" - "),
        MULTIPLY(" "),
        DIVIDE(" / ");
        
        public final String name;

        Op(String name) {
            this.name = name;
        }
    }

    public final Op operator;
    public final Expression left,right;

    public ExprBinary(Op operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression expand() {
        return switch (operator) {
            case PLUS ->
                new ExprBinary(operator, left.expand(), right.expand());
            default ->
                new ExprBinary(operator, left.expand(), right.expand());
        };
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExprBinary b
            && b.operator == operator
            && b.left.equals(left)
            && b.right.equals(right);
    }

    @Override 
    public String toString() {
        return "(%s)%s(%s)".formatted(left, operator.name, right);
    }
}
