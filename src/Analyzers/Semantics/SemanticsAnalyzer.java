package Analyzers.Semantics;

import Analyzers.Lexical.Symbol;
import Analyzers.Lexical.Token;
import Analyzers.Lexical.TokenType;
import Exceptions.SemanticsException;

import java.util.ArrayList;
import java.util.HashMap;

public class SemanticsAnalyzer {
    private final ArrayList<Token> parsedTokens;
    private final HashMap<String, ArrayList<Token>> declarationsMap;
    private final ArrayList<Symbol> symbolsTable;

    public SemanticsAnalyzer(ArrayList<Token> parsedTokens, HashMap<String, ArrayList<Token>> declarationsMap, ArrayList<Symbol> symbolsTable) {
        this.parsedTokens = parsedTokens;
        this.declarationsMap = declarationsMap;
        this.symbolsTable = symbolsTable;
    }

    public void analyze() throws SemanticsException {
        Token tokenToAnalyze, leftOperand, rightOperand;
        for (int i = 0; i < this.parsedTokens.size(); i++) {
            tokenToAnalyze = this.parsedTokens.get(i);
            if(tokenToAnalyze.getTokenType() == TokenType.IDENTIFIER)
                verifyMultipleDeclarationsError(tokenToAnalyze);
            else if (tokenToAnalyze.getTokenType() == TokenType.EQUAL) {
                leftOperand = this.parsedTokens.get(i == 0? i : i - 1);
                rightOperand = this.parsedTokens.get(i == this.parsedTokens.size() - 1? i : i + 1);
                verifyTypeCompatibilityInAttribution(leftOperand, rightOperand);
            }
        }
    }


    private void verifyMultipleDeclarationsError(Token tokenToSeek) throws SemanticsException {
        int declarationsCount = 0, lastDeclarationLine = 0;
        for (ArrayList<Token> declaredTokens: this.declarationsMap.values()) {
            for(Token token : declaredTokens)
                if(token.getTokenString().equals(tokenToSeek.getTokenString())) {
                    lastDeclarationLine = token.getLine();
                    declarationsCount++;
                }
        }
        if(declarationsCount > 1)
            throw new SemanticsException("Identificador '" + tokenToSeek.getTokenString() + "' ja foi declarado.", lastDeclarationLine);

    }

    private void verifyTypeCompatibilityInAttribution(Token leftOperand, Token rightOperand) throws SemanticsException {
        String leftOperandType = getOperandDataType(leftOperand);
        String rightOperandType;
        if(rightOperand.getTokenType() == TokenType.IDENTIFIER)
            rightOperandType = getOperandDataType(rightOperand);
        else
            rightOperandType = getLiteralType(rightOperand);

        if(!leftOperandType.equals(rightOperandType))
            throw new SemanticsException("Nao pode atribuir uma valor do tipo " + rightOperandType + " a um valor do tipo " + leftOperandType + ".", leftOperand.getLine());

    }

    private String getOperandDataType(Token identifierToken) {
        String operandType = "";
        for(Symbol symbolTableEntry : this.symbolsTable) {
            if(symbolTableEntry.getId().equals(identifierToken.getTokenString())) {
                operandType = symbolTableEntry.getType();
                break;
            }
        }
        return operandType;
    }

    private String getLiteralType(Token token) throws SemanticsException {
        String literalType;
        if(token.getTokenType() == TokenType.CHARACTER_CONSTANT)
            literalType = "char";
        else if (token.getTokenType() == TokenType.BOOLEAN_CONSTANT)
            literalType = "boolean";
        else
            throw new SemanticsException("Falha e avaliar o tipo de dado.", token.getLine());

        return literalType;
    }
}
