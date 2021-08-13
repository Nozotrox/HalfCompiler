package Analyzer;

import java.util.ArrayList;
import java.util.EmptyStackException;

public abstract  class StateMachine {
    protected State currentState;
    protected ArrayList<String> entries;

    public StateMachine(State initialState, String input) {
        this(initialState);
        String[] inputEntries = input.split(" ");
        for (String character : inputEntries)
            this.entries.add(character);

    }

    private StateMachine(State initialState) {
        this.currentState = initialState;
        this.entries = new ArrayList<>();
    }

    public abstract boolean validateInput();

}