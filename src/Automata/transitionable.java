package Automata;

public interface transitionable {
    boolean isPossible(String tag);
    machinestate getDestination();
}
