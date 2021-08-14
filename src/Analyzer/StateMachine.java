package Analyzer;

import java.util.ArrayList;
import java.util.EmptyStackException;

public abstract  class StateMachine {
    protected State currentState;
    protected ArrayList<String> entries;

    public StateMachine(State initialState, ArrayList<String> input) {
        this(initialState);
        this.entries = input;
    }

    private StateMachine(State initialState) {
        this.currentState = initialState;
        this.entries = new ArrayList<>();
    }

    public abstract boolean validateInput();

}