package Analyzers;

import Analyzers.Lexical.Token;
import Exceptions.EmptyAutomataStackException;

import javax.naming.OperationNotSupportedException;

public class PDTransintion extends Transition {
    private TokenType entryTokenType;
    private String requiredTopOfStackSymbol;
    private StackOperation operation;
    private String symbolForStackOperation;

    public PDTransintion (TokenType entryTokenType,  String requiredTopOfStackSymbol, StackOperation op, State destination) {
        super(entryTokenType.getName(), destination);
        this.entryTokenType = entryTokenType;
        this.requiredTopOfStackSymbol = requiredTopOfStackSymbol;
        this.operation = op;
        this.symbolForStackOperation = entryTokenType.getName();
    }

    public PDTransintion (TokenType entryTokenType,  String requiredTopOfStackSymbol, StackOperation op, State destination, String symbolForOperation) {
        this(entryTokenType, requiredTopOfStackSymbol, op, destination);
        this.symbolForStackOperation = symbolForOperation;
    }


    public boolean isTransitionalWith (Token token, String topOfStackSymbol) {
        boolean isOfEntryToken = token.getTokenType() == this.entryTokenType;
        boolean isValidTopOfStack = this.requiredTopOfStackSymbol.equals(topOfStackSymbol);
        return isOfEntryToken && isValidTopOfStack;
    }

    public void performTransition (AutomataStack automataStack) throws OperationNotSupportedException, EmptyAutomataStackException {
        automataStack.doOperation(this.operation, this.symbolForStackOperation);
    }

}
