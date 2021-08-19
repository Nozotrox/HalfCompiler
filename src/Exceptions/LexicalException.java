package Exceptions;

public class LexicalException extends CustomException {
    public LexicalException(String message, int line) {
        super(message, line);
    }

    @Override
    public String getErrorMessage() {
        return "Erro Léxico na linha " + this.line + ": \n\t" +  this.message;
    }

}
