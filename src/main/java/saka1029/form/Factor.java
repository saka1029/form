package saka1029.form;

public interface Factor extends Value, ToDebugString {
    FactorOperator operator();
    int power();
    Primary primary();
}
