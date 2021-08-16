package Analyzers;

import Exceptions.EmptyAutomataStackException;

import javax.naming.OperationNotSupportedException;
import java.util.Stack;

public class AutomataStack {
    public static final String DEFAULT_EMPTY_STACK_SYMBOL = "z0";
    private int lineInAnalysis;

    Stack<String> stack;
    String bottomOfStackSymbol;

    public AutomataStack() {
        this.stack = new Stack<>();
        this.bottomOfStackSymbol = AutomataStack.DEFAULT_EMPTY_STACK_SYMBOL;
        this.initializeStack();
    }

    public AutomataStack(String bottomOfStackSymbol) {
        this();
        this.bottomOfStackSymbol = bottomOfStackSymbol;
        initializeStack();
    }

    private void initializeStack() {
        this.stack = new Stack<>();
        this.stack.push(this.bottomOfStackSymbol);
    }

    public void doOperation(StackOperation op, String obj) throws OperationNotSupportedException, EmptyAutomataStackException {

        switch (op) {
            case INSERT: {
                insertToStack(obj);
                break;
            }
            case ERASE: {
                removeFromStack();
                break;
            }
            case REPLACE: {
                replaceInStack(obj);
                break;
            }
            case MAINTAIN: {
                break;
            }
            default:
                throw new OperationNotSupportedException("Invalid operation for automata stack.");
        }

    }

    private void insertToStack(String obj) {
        this.stack.add(obj);
    }

    private void removeFromStack() throws EmptyAutomataStackException {
        if (this.stack.peek().equals(this.bottomOfStackSymbol))
            throw new EmptyAutomataStackException("Can't operate on empty stack!");
        this.stack.pop();
    }

    private void replaceInStack(String obj) throws EmptyAutomataStackException {
        removeFromStack();
        insertToStack(obj);
    }

    public String getTopOfStack() {
        return this.stack.peek();
    }

    public int getSize() {
        return this.stack.size() - 1;
    }
}
