package saka1029.form;

public class FormException extends RuntimeException {
    public FormException(String format, Object... args) {
        super(format.formatted(args));
    }
    public FormException(Throwable t) {
        super(t);
    }
}
