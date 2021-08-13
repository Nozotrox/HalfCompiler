package Analyzer;

import Exceptions.EmptyAutomataStackException;

import javax.naming.OperationNotSupportedException;

public class PDTransintion extends Transition {
    private String requiredTopOfStackSymbol;
    private StackOperation operation;
    private String symbolForStackOperation;

    public PDTransintion (String entrySymbol,  String requiredTopOfStackSymbol, StackOperation op, State destination) {
        super(entrySymbol, destination);
        this.requiredTopOfStackSymbol = requiredTopOfStackSymbol;
        this.operation = op;
        this.symbolForStackOperation = this.entrySymbol;
    }

    public PDTransintion (String entrySymbol,  String requiredTopOfStackSymbol, StackOperation op, State destination, String symbolForOperation) {
        this(entrySymbol, requiredTopOfStackSymbol, op, destination);
        this.symbolForStackOperation = symbolForOperation;
    }


    public boolean isTransitionalWith (String entrySymbol, String topOfStackSymbol) {
        boolean isOfEntrySymbol = this.isTaggedWith(entrySymbol);
        boolean isValidTopOfStack = this.requiredTopOfStackSymbol.equals(topOfStackSymbol);
        return isOfEntrySymbol && isValidTopOfStack;
    }

    public void performTransition (AutomataStack automataStack) throws OperationNotSupportedException, EmptyAutomataStackException {
        automataStack.doOperation(this.operation, this.symbolForStackOperation);
    }

}
