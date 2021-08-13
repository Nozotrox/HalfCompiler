package Automata;

public class Automata implements statemachine {
    private State currentState;
    private String word;
    private String tree;

    public Automata (State initialState) {
        this.currentState = initialState;
        this.tree = this.currentState.getName();
    }

    public void inputWord(String word) {
        this.word = word;
    }


    @Override
    public void switchState() {
        this.currentState = currentState.getNextState();
        writeOnTree();
    }

    @Override
    public boolean canStop() {
        return currentState.isFinal();
    }

    private boolean cantStop() {
        return !this.canStop();
    }

    private void writeOnTree() {
        this.tree += " --> " + this.currentState.getName();
    }

    public void printTree() {
        System.out.println(tree);
    }

    private String  extractNextTokken() {
        String nextToken;
        if(this.word.length() > 1) {
            nextToken = this.word.substring(0,1);
            this.word = this.word.substring(1);
        } else  {
            nextToken = this.word;
            this.word = "";
        }
        return  nextToken;
    }

    public void validate() {
        String nextToken;
        boolean isValid = true;
        while (!this.word.isEmpty()) {
            nextToken = extractNextTokken();
            if(this.currentState.accepts(nextToken)) {
                switchState();
            } else {
                isValid = false;
                break;
            }
        }

        if(isValid && this.canStop())
            System.out.println("Valid!");
        else
            System.out.println("Invalid");
    }
}
