package Exceptions;

public class SemanticsException extends CustomException {
    public SemanticsException(String message, int line) {
        super(message, line);
    }

    @Override
    public String getErrorMessage() {
        return "Erro Semântico na linha " + this.line + ": \n\t" +  this.message;
    }
}
