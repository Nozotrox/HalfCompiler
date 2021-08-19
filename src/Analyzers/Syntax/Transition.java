package Analyzers.Syntax;

import Analyzers.Syntax.State;

public class Transition {
    protected String entrySymbol;
    protected State destination;

    public Transition(String entrySymbol, State destination) {
        this.entrySymbol = entrySymbol;
        this.destination = destination;
    }

    public boolean isTaggedWith(String entrySymbol) {
        return  entrySymbol.equals(this.entrySymbol);
    }

    public State getDestinationState() {
        return this.destination;
    }
}
