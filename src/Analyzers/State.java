package Analyzers;

import Analyzers.Lexical.Token;
import Exceptions.EmptyAutomataStackException;
import Exceptions.StateNotFoundException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

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

    public State moveToNextStateWith(Token entryToken, AutomataStack stack) throws EmptyAutomataStackException, OperationNotSupportedException, StateNotFoundException {
        State nextState = null;
        for (PDTransintion transition : this.transitions) {
            if(transition.isTransitionalWith(entryToken, stack.getTopOfStack())) {
                nextState = transition.getDestinationState();
                transition.performTransition(stack);
                break;
            }
        }

        if(nextState == null)
            throw new StateNotFoundException("Could not find state of transition " + entryToken.getTokenString() + ".", entryToken.getLine());

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
