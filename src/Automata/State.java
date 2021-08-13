package Automata;

import java.util.ArrayList;

public class State implements machinestate{
    private String name;
    private ArrayList<Transition> transitions;
    private State nextPossibleState;
    private boolean isFinal;

    public State(String name, boolean isFinal) {
        this.name = name;
        this.isFinal = isFinal;
        this.transitions = new ArrayList<>();
    }

    public State(boolean isFinal) {
        this("-", isFinal);
    }

    public State() {
        this("-", false);
    }

    public String getName() {
        return name;
    }

    public boolean accepts(String tag) {
        boolean canTransition = false;
        for(Transition transition : transitions)  {
            if(transition.isPossible(tag)){
                canTransition = true;
                nextPossibleState = (State) transition.getDestination();
                break;
            }
        }

        return canTransition;
    }

    public State getNextState(String tag) {
        if(nextPossibleState == null)
            this.accepts(tag);
        return  nextPossibleState;
    }

    public State getNextState() {
        return nextPossibleState;
    }


    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public void addTransition(transitionable transition) {
        transitions.add((Transition) transition);
    }
}
