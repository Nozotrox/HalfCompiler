package Automata;

public class Validating {

    public static void main(String[] args) {

        final State A = new State("A",false);
        final State B = new State("B", false);
        final State C = new State("C", true);
        final State D = new State("D", true);



        Transition transition1 = new Transition("a", B);
        Transition transition2 = new Transition("a", C);
        Transition transition3 = new Transition("a", B);
        Transition transition4 = new Transition("b", D);
        Transition transition5 = new Transition("b", D);

        A.addTransition(transition1);
        B.addTransition(transition2);
        C.addTransition(transition3);
        C.addTransition(transition4);
        D.addTransition(transition5);

        Automata machine = new Automata(A);
        machine.inputWord("aaabb");
        machine.validate();
        machine.printTree();

    }
}
