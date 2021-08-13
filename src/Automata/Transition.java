package Automata;

public class Transition implements transitionable {

    private String tag;
    private State destination;

    public Transition() {
       this(null, null);
    }

    public Transition(String tag, State destination) {
        this.tag = tag;
        this.destination = destination;
    }

    @Override
    public machinestate getDestination() {
        return destination;
    }

    @Override
    public boolean isPossible(String tag) {
        return this.tag.equals(tag);
    }


}
