package Exceptions;

public class EmptyAutomataStackException extends Exception {
    private String line;
    public EmptyAutomataStackException(String message) {
        super(message);
    }
}
