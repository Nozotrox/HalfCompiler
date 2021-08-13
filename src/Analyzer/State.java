package Analyzer;

import Exceptions.EmptyAutomataStackException;
import Exceptions.StateNotFoundException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Stack;

public class State {
    private String name;
    private boolean isFinalState;
    private boolean isInitialState;
    private ArrayList<PDTransintion> transitions;

    public State(String name, boolean isFinalState, boolean isInitialState) {
        this.name = name;
        this.isFinalState = isFinalState;
        this.isInitialState = isInitialState;
        this.transitions = new ArrayList<>();
    }

    public State(String name) {
        this(name, false, false);
    }

    public State moveToNextStateWith(String entrySymbol, AutomataStack stack) throws EmptyAutomataStackException, OperationNotSupportedException {
        State nextState = null;
        for (PDTransintion transition : this.transitions) {
            if(transition.isTransitionalWith(entrySymbol, stack.getTopOfStack())) {
                nextState = transition.getDestinationState();
                transition.performTransition(stack);
                break;
            }
        }

        if(nextState == null)
            throw new StateNotFoundException("Could not find state of transition " + entrySymbol + ".");

        return nextState;
    }

    public void addTransition(PDTransintion transition) {
        transitions.add(transition);
    }

    public boolean isFinalState() {
        return this.isFinalState;
    }

    public boolean isInitialState() {
        return this.isInitialState;
    }

    public String getName() {
        return name;
    }
}
