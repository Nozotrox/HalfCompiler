package Analyzer;

import Exceptions.EmptyAutomataStackException;

import javax.naming.OperationNotSupportedException;

public class PushDownAutomata extends  StateMachine {
    private StateMachineProcessPrinter printer;
    private int currentIndex = 0;
    AutomataStack stack;

    public PushDownAutomata(State initialState, String entries) {
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

        System.out.println("Stack Size: "  + this.stack.getSize());
        System.out.println("Stack TOP: "  + this.stack.getTopOfStack());

        return true;
    }

    //::: ITERATIVE IMPLEMENTATION
    private boolean processInput() {
        try {
            for(String entrySymbol : this.entries) {
                currentState = currentState.moveToNextStateWith(entrySymbol, this.stack);
                this.printer.printProcess(currentState, entrySymbol);
            }
        } catch (EmptyAutomataStackException | OperationNotSupportedException e) {
            e.printStackTrace();
        }

        return this.currentState.isFinalState();
    }



}
