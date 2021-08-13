package Automata;

public interface machinestate {
    boolean isFinal();
    void addTransition(transitionable transition);
}
