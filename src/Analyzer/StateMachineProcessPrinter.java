package Analyzer;

import java.util.ArrayList;

public class StateMachineProcessPrinter {
    private ArrayList<String> inputs;
    private int indentationLevel = 0;
    private AutomataStack stack;
    private String prevStateName;
    public StateMachineProcessPrinter(AutomataStack stack) {
        this.stack = stack;
    }

    public void printProcess(State currentState, String entrySymbol) {
        if(currentState.isInitialState())
            System.out.print(currentState.getName());
        else {
            if (hasStackChanged())
                printIndentedLine(this.prevStateName);
            System.out.print(" --" + entrySymbol + "--> " + currentState.getName());
        }
        this.prevStateName = currentState.getName();
    }

    private boolean hasStackChanged() {
        return this.indentationLevel != this.stack.getSize();
    }

    private void printIndentedLine(String string) {
        this.indentationLevel = this.stack.getSize();
        System.out.print("\n");
        printIndentations();
        System.out.print(string);
    }

    private void printIndentations() {
        for(int i = 0; i < this.indentationLevel; i++)
            System.out.print("\t");
    }
}
