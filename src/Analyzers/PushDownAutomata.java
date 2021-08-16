package Analyzers;

import Analyzers.Lexical.Token;
import Exceptions.CustomException;
import Exceptions.EmptyAutomataStackException;
import Exceptions.StateNotFoundException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

public class PushDownAutomata extends  StateMachine {
    private StateMachineProcessPrinter printer;
    private int currentIndex = 0;
    AutomataStack stack;

    public PushDownAutomata(State initialState, ArrayList<Token> entries) {
        super(initialState, entries);
        this.stack = new AutomataStack();
        this.printer = new StateMachineProcessPrinter(this.stack);
    }

    @Override
    public boolean validateInput() {
        printer.printProcess(this.currentState, "");
        boolean isValid = processInput();
        System.out.println();
        if(isValid)
            System.out.println("::: VALID INPUT");
        else
            System.out.println("::: INVALID INPUT");
        return true;
    }

    //::: ITERATIVE IMPLEMENTATION
    private boolean processInput() {
        try {
            for(Token entryToken : this.entries) {
                currentState = currentState.moveToNextStateWith(entryToken, this.stack);
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
