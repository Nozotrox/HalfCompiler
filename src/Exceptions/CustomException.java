package Exceptions;

public abstract class CustomException extends Exception {
    protected int line;
    protected String message;
    public CustomException(String message, int line) {
        super(message);
        this.line = line;
        this.message = message;
    }

    public abstract String getErrorMessage();

    public int getLine() {
        return line;
    }
}
