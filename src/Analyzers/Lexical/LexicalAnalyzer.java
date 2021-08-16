package Analyzers.Lexical;

import Analyzers.TokenType;
import Exceptions.CustomException;
import Exceptions.TypeErrorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LexicalAnalyzer {
    private Tokenizer tokenizer;
    private File fileToAnalyze;
    private ArrayList<Token> parsedTokens;

    public LexicalAnalyzer(File file) {
        this.fileToAnalyze = file;
        this.tokenizer = new Tokenizer(file);
    }

    public ArrayList<Token> getParsedTokens() {
        return parsedTokens;
    }

    public boolean analyze() throws IOException {
        boolean wasSuccessful = true;
        try {
            this.tokenizer.parseTokens();
            this.parsedTokens = this.tokenizer.getTokens();
            verifyLexicalErrors();

        } catch (TypeErrorException e) {
            System.out.println(((CustomException) e).getErrorMessage());
            wasSuccessful = false;
        }

        return wasSuccessful;
    }

    private void verifyLexicalErrors() throws TypeErrorException {
        verifyIdentifierErrors();
        verifyMalformedElements();
    }

    private void verifyIdentifierErrors() throws TypeErrorException {
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.IDENTIFIER) {
                verifyInvalidIdentifierSymbol(tokenToAnalyze);
                verifyNoDeclarationError(tokenToAnalyze);
                verifyMultipleDeclarationsError(tokenToAnalyze);
            }
        }
    }

    private void verifyNoDeclarationError(Token tokenToVerify) throws TypeErrorException {
        if(hasNotBeenDeclared(tokenToVerify))
            throw new TypeErrorException("Identifier '" + tokenToVerify.getTokenString() + "' has not been declared.", tokenToVerify.getLine());
    }

    private boolean hasNotBeenDeclared(Token tokenToVerify) {
        boolean isNotDeclared = true;
        Token currentToken, prevToken;
        for(int i = 0; i < this.parsedTokens.size(); i++) {
            currentToken = this.parsedTokens.get(i);
            prevToken = this.parsedTokens.get(i == 0? i : i - 1);
            if(tokenToVerify.getTokenString().equals(currentToken.getTokenString())) {
               if (isDeclarationStatement(prevToken, currentToken)) {
                    isNotDeclared = false;
                    break;
               }
            }
        }
        return  isNotDeclared;
    }

    private boolean isDeclarationStatement(Token prevToken, Token currentToken) {
        boolean isDeclaration = true;
        isDeclaration = isDeclaration && (currentToken != null);
        isDeclaration = isDeclaration && (prevToken.getTokenType() == TokenType.PRIMITIVE_TYPE);
        return isDeclaration;
    }

    private void verifyMultipleDeclarationsError(Token tokenToSeek) throws TypeErrorException {
        int declarationsCount = 0;
        int lastDeclarationLine = 0;
        Token iToken, prevToken;
        for(int i = 0; i < this.parsedTokens.size() ; i++) {
            iToken = this.parsedTokens.get(i);
            if(iToken.getTokenString().equals(tokenToSeek.getTokenString())) {
                prevToken = this.parsedTokens.get(i == 0? i : i - 1);
                if(isDeclarationStatement(prevToken, iToken)) {
                    declarationsCount++;
                    lastDeclarationLine = iToken.getLine();
                }
            }
        }

        if(declarationsCount > 1)
            throw new TypeErrorException("Identifier '" + tokenToSeek.getTokenString() + "' has already been declared.", lastDeclarationLine);

    }

    private void verifyInvalidIdentifierSymbol (Token tokenToSeek) throws TypeErrorException {
        Token iToken;
        Token nextToken;
        for(int i = 0; i < this.parsedTokens.size(); i++) {
            iToken = this.parsedTokens.get(i);
            if(iToken.getTokenString().equals(tokenToSeek.getTokenString())) {
                nextToken = this.parsedTokens.get(i == this.parsedTokens.size() - 1? i : i + 1);
                if(isNotAttributionStatement(iToken, nextToken))
                    throw new TypeErrorException("Invalid Symbol for " + iToken.getTokenString(), iToken.getLine());
                else
                    break;
            }
        }

    }

    private boolean isNotAttributionStatement(Token currentToken, Token nextToken) {
        return !isAttributionStatement(currentToken, nextToken);
    }

    private boolean isAttributionStatement(Token currentToken, Token nextToken) {
        boolean isAttribution = true;
        isAttribution = isAttribution && (currentToken.getTokenType() == TokenType.IDENTIFIER);
        isAttribution = isAttribution && nextToken.getTokenString().equals("=");
        return isAttribution;
    }

    private void verifyMalformedElements() throws TypeErrorException {
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.CHARACTER_CONSTANT)
                verifyIfCharacterIsMalformed(tokenToAnalyze);
        }
    }

    private void verifyIfCharacterIsMalformed(Token tokenToSeek) throws TypeErrorException {
        if(tokenToSeek.getTokenString().length() > 1)
            throw new TypeErrorException("Malformed character literal " + tokenToSeek.getTokenString(), tokenToSeek.getLine());
    }


}
