package Exceptions;

public class StateNotFoundException extends RuntimeException {
    private String line;
    public StateNotFoundException(String message) {
        super(message);
    }
}
