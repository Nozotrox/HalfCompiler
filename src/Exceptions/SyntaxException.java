package Exceptions;

public class SyntaxException extends CustomException {
    public SyntaxException(String message, int line) {
        super(message, line);
    }

    @Override
    public String getErrorMessage() {
        return "Erro sintático na linha " + this.line + ": \n\t" +  this.message;
    }
}
