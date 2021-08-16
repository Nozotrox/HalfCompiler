package Exceptions;

public class TypeErrorException extends CustomException {
    public TypeErrorException(String message, int line) {
        super(message, line);
    }

    @Override
    public String getErrorMessage() {
        return "TypeError on line " + this.line + ": \n\t" +  this.message;
    }

}
