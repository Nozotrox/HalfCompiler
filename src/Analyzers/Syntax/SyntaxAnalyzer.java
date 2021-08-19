package Analyzers.Syntax;

import Analyzers.Lexical.Token;
import Analyzers.Lexical.TokenType;
import Exceptions.SyntaxException;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;

public class SyntaxAnalyzer {
    private final ArrayList<Token> tokensToParse;
    private final boolean isToPrintProcess;

    private PushDownAutomata pushDownAutomata;

    public SyntaxAnalyzer(ArrayList<Token> tokens, boolean isToPrintProcess) {
        tokensToParse = tokens;
        this.isToPrintProcess = isToPrintProcess;
    }

    public void analyze() throws SyntaxException {
        generateAutomata();
        pushDownAutomata.validateInput();
    }

    private void generateAutomata() {
        //:: MAIN STATES
        State q0 = new State("q0", false, true);
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        State ef = new State("ef", true, false);
        //:: INTERMEDIARY STATES
        State qa = new State("qa");
        State qc = new State("qc");
        State qd = new State("qd");
        State qe = new State("qe");
        State qf = new State("qf");
        State qg = new State("qg");
        State qh = new State("qh");
        State qi = new State("qi");

        //::>> Building Automata
        createTransitionFromTo(q0, "do z0 z0", StackOperation.MAINTAIN, qa);
        createTransitionFromTo(q0, "type_tkn z0 z1", StackOperation.INSERT, qf);
        createTransitionFromTo(q0, "id_tkn z0 z1", StackOperation.INSERT, qg);

        createTransitionFromTo(qa, "{ do do", StackOperation.MAINTAIN, q1);
        createTransitionFromTo(qa, "{ z0 z0", StackOperation.MAINTAIN, q1);

        createTransitionFromTo(q1, "} do do", StackOperation.MAINTAIN, qc);
        createTransitionFromTo(q1, "} z0 z0", StackOperation.MAINTAIN, qc);
        createTransitionFromTo(q1, "do z0 do", StackOperation.INSERT, qa);
        createTransitionFromTo(q1, "do do do", StackOperation.INSERT, qa);
        createTransitionFromTo(q1, "id_tkn do do", StackOperation.MAINTAIN, qg);
        createTransitionFromTo(q1, "id_tkn z0 z0", StackOperation.MAINTAIN, qg);
        createTransitionFromTo(q1, "type_tkn z0 z0", StackOperation.MAINTAIN, qf);
        createTransitionFromTo(q1, "type_tkn do do", StackOperation.MAINTAIN, qf);

        //:::>> DECLARATION AND INITIALIZATION VALIDATION
            createTransitionFromTo(qf,"id_tkn z1 z1",StackOperation.MAINTAIN, qg);
            createTransitionFromTo(qf,"id_tkn z0 z0",StackOperation.MAINTAIN, qg);
            createTransitionFromTo(qf,"id_tkn do do",StackOperation.MAINTAIN, qg);

            createTransitionFromTo(qg, "= z1 z1", StackOperation.MAINTAIN, qh);
            createTransitionFromTo(qg, "= do do", StackOperation.MAINTAIN, qh);
            createTransitionFromTo(qg, "= z0 z0", StackOperation.MAINTAIN, qh);
            createTransitionFromTo(qg, ", z1 z1", StackOperation.MAINTAIN, qf);
            createTransitionFromTo(qg, ", do do", StackOperation.MAINTAIN, qf);
            createTransitionFromTo(qg, ", z0 z0", StackOperation.MAINTAIN, qf);
            createTransitionFromTo(qg, "; do do", StackOperation.MAINTAIN, q1);
            createTransitionFromTo(qg, "; z0 z0", StackOperation.MAINTAIN, q1);
            createTransitionFromTo(qg, "; z1 E", StackOperation.ERASE, ef);


            createTransitionFromTo(qh, "charLit_tkn z1 z1", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "charLit_tkn do do", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "charLit_tkn z0 z0", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "boolean_tkn z1 z1", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "boolean_tkn do do", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "boolean_tkn z0 z0", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "id_tkn z1 z1", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "id_tkn do do", StackOperation.MAINTAIN, qi);
            createTransitionFromTo(qh, "id_tkn z0 z0", StackOperation.MAINTAIN, qi);

            createTransitionFromTo(qi, "; do do", StackOperation.MAINTAIN, q1);
            createTransitionFromTo(qi, "; z0 z0", StackOperation.MAINTAIN, q1);
            createTransitionFromTo(qi, "; z1 E", StackOperation.ERASE, ef);
            createTransitionFromTo(qi, ", z1 z1", StackOperation.MAINTAIN, qf);
            createTransitionFromTo(qi, ", do do", StackOperation.MAINTAIN, qf);
            createTransitionFromTo(qi, ", z0 z0", StackOperation.MAINTAIN, qf);

        createTransitionFromTo(qc, "while z0 z0", StackOperation.MAINTAIN, qd);
        createTransitionFromTo(qc, "while do do", StackOperation.MAINTAIN, qd);

        createTransitionFromTo(qd, "( z0 z0", StackOperation.MAINTAIN, q2);
        createTransitionFromTo(qd, "( do do", StackOperation.MAINTAIN, q2);

        createTransitionFromTo(q2, "boolean_tkn do do", StackOperation.MAINTAIN, q3);
        createTransitionFromTo(q2, "boolean_tkn z0 z0", StackOperation.MAINTAIN, q3);

        createTransitionFromTo(q3, ") z0 z0", StackOperation.MAINTAIN, qe);
        createTransitionFromTo(q3, ") do do", StackOperation.MAINTAIN, qe);

        createTransitionFromTo(qe, "; z0 z0", StackOperation.MAINTAIN, ef);
        createTransitionFromTo(qe, "; do E", StackOperation.ERASE, q1);

        createTransitionFromTo(ef, "do z0 z0", StackOperation.MAINTAIN, qa);
        createTransitionFromTo(ef, "type_tkn z0 z1", StackOperation.INSERT, qf);
        createTransitionFromTo(ef, "id_tkn z0 z1", StackOperation.INSERT, qg);

        pushDownAutomata = new PushDownAutomata(q0, this.tokensToParse, this.isToPrintProcess);

    }

    private void createTransitionFromTo(State from, String transitionName, StackOperation op, State to) {
        int entrySymbolIndex = 0, topOfStackIndex = 1, operationSymbolIndex = 2;
        String[] transitionSymbols = transitionName.split(" ");
        String topOfStackSymbol = transitionSymbols[topOfStackIndex];
        String operationSymbol = transitionSymbols[operationSymbolIndex];
        String entrySymbol = transitionSymbols[entrySymbolIndex];

        try {
            TokenType tokenType = getTokenByText(entrySymbol);
            PDTransintion transition = new PDTransintion(tokenType,topOfStackSymbol, op, to, operationSymbol);
            from.addTransition(transition);
        } catch (OperationNotSupportedException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private TokenType getTokenByText(String tokenText) throws OperationNotSupportedException {
        TokenType tokenType;
        switch (tokenText) {
            case "charLit_tkn" : tokenType = TokenType.CHARACTER_CONSTANT;break;
            case "boolean_tkn": tokenType = TokenType.BOOLEAN_CONSTANT;break;
            case "do": tokenType = TokenType.DO;break;
            case "{": tokenType  = TokenType.OPEN_CURLY_BRACKET;break;
            case "}": tokenType = TokenType.CLOSE_CURLY_BRACKET;break;
            case "(": tokenType = TokenType.OPEN_BRACKET;break;
            case ")": tokenType = TokenType.CLOSE_BRACKET;break;
            case "while": tokenType = TokenType.WHILE;break;
            case "true":;
            case "false": tokenType = TokenType.BOOLEAN_CONSTANT;break;
            case ";": tokenType = TokenType.SEMI_COLON;break;
            case "id_tkn": tokenType = TokenType.IDENTIFIER;break;
            case "=": tokenType = TokenType.EQUAL;break;
            case "type_tkn": tokenType = TokenType.PRIMITIVE_TYPE;break;
            case ",": tokenType = TokenType.COMMA;break;
            default:
                throw new OperationNotSupportedException("Falha ao construir o automato");
        }
        return tokenType;
    }

}
