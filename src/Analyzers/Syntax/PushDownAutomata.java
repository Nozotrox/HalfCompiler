package Analyzers.Syntax;

import Analyzers.Lexical.Token;
import Analyzers.StateMachineProcessPrinter;
import Exceptions.CustomException;
import Exceptions.EmptyAutomataStackException;
import Exceptions.StateNotFoundException;
import Exceptions.SyntaxException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

public class PushDownAutomata extends  StateMachine {
    private final StateMachineProcessPrinter printer;
    private final boolean isToPrintProcess;
    AutomataStack stack;

    public PushDownAutomata(State initialState, ArrayList<Token> entries, boolean isToPrintProcess) {
        super(initialState, entries);
        this.stack = new AutomataStack();
        this.printer = new StateMachineProcessPrinter(this.stack);
        this.isToPrintProcess = isToPrintProcess;
    }

    @Override
    public boolean validateInput() throws SyntaxException {
        if(this.isToPrintProcess)
            printer.printProcess(this.currentState, "");
        boolean isValid = processInput();
        if(!isValid)
            throw new SyntaxException("Forma inválida de construção.", -1);
        return isValid;
    }

    //::: ITERATIVE IMPLEMENTATION
    private boolean processInput() {
        try {
            for(Token entryToken : this.entries) {
                currentState = currentState.moveToNextStateWith(entryToken, this.stack);
                if(this.isToPrintProcess)
                    this.printer.printProcess(currentState, entryToken.getTokenString());
            }
        } catch (OperationNotSupportedException | StateNotFoundException e) {
            System.out.println(((CustomException) e).getErrorMessage());
        } catch (EmptyAutomataStackException e) {
            e.printStackTrace();
        }
        return this.currentState.isFinalState();
    }

}
