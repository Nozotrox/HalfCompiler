package Analyzers.Syntax;

import Analyzers.Lexical.Token;
import Exceptions.EmptyAutomataStackException;
import Exceptions.StateNotFoundException;
import Exceptions.SyntaxException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

public abstract  class StateMachine {
    protected State currentState;
    protected ArrayList<Token> entries;

    public StateMachine(State initialState, ArrayList<Token> input) {
        this(initialState);
        this.entries = input;
    }

    private StateMachine(State initialState) {
        this.currentState = initialState;
        this.entries = new ArrayList<>();
    }

    public abstract boolean validateInput() throws SyntaxException, EmptyAutomataStackException, OperationNotSupportedException, StateNotFoundException;

}