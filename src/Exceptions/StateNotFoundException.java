package Exceptions;

public class StateNotFoundException extends CustomException {
    public StateNotFoundException(String message, int line) {
        super(message, line);
    }

    @Override
    public String getErrorMessage() {
        return "Erro sintático na linha " + this.line + ": \n\t" + this.message;
    }

}
