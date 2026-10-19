package saka1029.form;

public enum FactorOperator {
    MULTIPLY("*"), DIVIDE("/");
    public final String string;
    FactorOperator(String string) {
        this.string = string;
    }
    @Override
    public String toString() {
        return string;
    }
}
