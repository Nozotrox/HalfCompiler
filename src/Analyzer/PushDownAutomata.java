package Analyzer;

import Exceptions.EmptyAutomataStackException;

import javax.naming.OperationNotSupportedException;

public class PushDownAutomata extends  StateMachine {
    private StateMachineProcessPrinter printer;
    private int currentIndex = 0;
    AutomataStack stack;

    public PushDownAutomata(State initialState, String entries) {
        super(initialState, entries);
        this.stack = new AutomataStack();
        this.printer = new StateMachineProcessPrinter(this.stack);
    }

    @Override
    public boolean validateInput() {
        printer.printProcess(this.currentState, "");
        boolean isValid = processInput();
        System.out.println();
        if(isValid)
            System.out.println("::: VALID INPUT");
        else
            System.out.println("::: INVALID INPUT");

        System.out.println("Stack Size: "  + this.stack.getSize());
        System.out.println("Stack TOP: "  + this.stack.getTopOfStack());

        return true;
    }

    //::: ITERATIVE IMPLEMENTATION
    private boolean processInput() {
        try {
            for(String entrySymbol : this.entries) {
                currentState = currentState.moveToNextStateWith(entrySymbol, this.stack);
                this.printer.printProcess(currentState, entrySymbol);
            }
        } catch (EmptyAutomataStackException | OperationNotSupportedException e) {
            e.printStackTrace();
        }

        return this.currentState.isFinalState();
    }

    public static void main(String[] args){
        String empty = AutomataStack.DEFAULT_EMPTY_STACK_SYMBOL;
        StackOperation maintain = StackOperation.MAINTAIN;
        //:: MAIN STATES
        State q0 = new State("q0", false, true);
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        State ef = new State("ef", true, false);
        //:: INTERMEDIARY STATES
        State qa = new State("qa");
        State qb = new State("qb");
        State qc = new State("qc");
        State qd = new State("qd");
        State qe = new State("qe");


        //::: TRANSITIONS
        PDTransintion tDo1 = new PDTransintion("do", empty, maintain, qa);
        PDTransintion tDo2 = new PDTransintion("do", empty, StackOperation.INSERT, qa);
        PDTransintion tDo3 = new PDTransintion("do", "do", StackOperation.INSERT, qa);

        PDTransintion tOcb1 = new PDTransintion("{", empty, maintain, q1);
        PDTransintion tOcb2 = new PDTransintion("{", "do", maintain, q1);

        PDTransintion tSpace1 = new PDTransintion("_", empty, maintain, qb);
        PDTransintion tSpace2 = new PDTransintion("_", "do", maintain, qb);

        PDTransintion tCcb1 = new PDTransintion("}", empty, maintain, qc);
        PDTransintion tCcb2 = new PDTransintion("}", "do", maintain, qc);

        PDTransintion tWhile1 = new PDTransintion("while", empty, maintain, qd);
        PDTransintion tWhile2 = new PDTransintion("while", "do", maintain, qd);

        PDTransintion tOb1 = new PDTransintion("(", empty, maintain, q2);
        PDTransintion tOb2 = new PDTransintion("(", "do", maintain, q2);

        PDTransintion tTrue1 = new PDTransintion("true", empty, maintain, q3);
        PDTransintion tTrue2 = new PDTransintion("true", "do", maintain, q3);
        PDTransintion tFalse1 = new PDTransintion("false", empty, maintain, q3);
        PDTransintion tFalse2 = new PDTransintion("false", "do", maintain, q3);


        PDTransintion tCb1 = new PDTransintion(")", empty, maintain, qe);
        PDTransintion tCb2 = new PDTransintion(")", "do", maintain, qe);

        PDTransintion tSc1 = new PDTransintion(";", "do", StackOperation.ERASE, q1);
        PDTransintion tSc2 = new PDTransintion(";", empty, maintain, ef);


        //::: ADDING TRANSITIONS TO STATE
        q0.addTransition(tDo1);

        qa.addTransition(tOcb1);
        qa.addTransition(tOcb2);

        q1.addTransition(tDo2);
        q1.addTransition(tDo3);
        q1.addTransition(tSpace1);
        q1.addTransition(tSpace2);

        qb.addTransition(tCcb1);
        qb.addTransition(tCcb2);

        qc.addTransition(tWhile1);
        qc.addTransition(tWhile2);

        qd.addTransition(tOb1);
        qd.addTransition(tOb2);

        q2.addTransition(tTrue1);
        q2.addTransition(tTrue2);
        q2.addTransition(tFalse1);
        q2.addTransition(tFalse2);

        q3.addTransition(tCb1);
        q3.addTransition(tCb2);

        qe.addTransition(tSc1);
        qe.addTransition(tSc2);

        PushDownAutomata pda = new PushDownAutomata(q0, "do { do { _ } while ( true ) ; do { do { do { _ } while ( true ) ; _ } while ( true ) ; _ } while ( true ) ; _ } while ( true ) ;");
        pda.validateInput();
    }

}
