package Exceptions;

public class EmptyAutomataStackException extends Exception {
    private int line;
    public EmptyAutomataStackException(String message) {
        super(message);
        this.line = line;
    }
}
