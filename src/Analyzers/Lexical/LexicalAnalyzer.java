package Analyzers.Lexical;

import Analyzers.TokenType;
import Exceptions.CustomException;
import Exceptions.TypeErrorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LexicalAnalyzer {
    private Tokenizer tokenizer;
    private File fileToAnalyze;
    private ArrayList<Token> parsedTokens;
    private ArrayList<Symbol> symbolTable;
    HashMap<String, ArrayList<Token>> declarationsMap = new HashMap<>();

    public LexicalAnalyzer(File file) {
        this.fileToAnalyze = file;
        this.tokenizer = new Tokenizer(file);
        this.symbolTable = new ArrayList<>();
    }

    public ArrayList<Token> getParsedTokens() {
        return parsedTokens;
    }

    public void analyze() throws IOException, TypeErrorException {
        this.tokenizer.parseTokens();
        this.parsedTokens = this.tokenizer.getTokens();
        verifyLexicalErrors();
        generateSymbolTable();
    }

    public void printSymbolTable() {
        if(symbolTable.isEmpty())
            System.out.println("The symbol table is empty!");
        else {
            for(Symbol symbol : symbolTable)
                symbol.printSymbol();
        }
    }

    public void printLexicalTable() {
        if(parsedTokens.isEmpty())
            System.out.println("The lexical table is empty!");
        else {
            for(Token token : this.parsedTokens)
                token.printToken();
        }
    }

    private void verifyLexicalErrors() throws TypeErrorException {
        verifyIdentifierErrors();
        verifyMalformedElements();
    }

    private void verifyIdentifierErrors() throws TypeErrorException {
                collectAllDeclarations();
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.IDENTIFIER) {
                verifyInvalidIdentifierSymbol(tokenToAnalyze);
                verifyNoDeclarationError(tokenToAnalyze);
                verifyMultipleDeclarationsError(tokenToAnalyze);
            }
        }
    }

    private void collectAllDeclarations() {

        boolean isCollectingDeclarations = false;
        String declarationType = "";
        for(Token token : this.parsedTokens) {
            if(isCollectingDeclarations)
                if(token.getTokenType() == TokenType.IDENTIFIER)
                    addToDeclarationMap(declarationType, token);

            if(token.getTokenType() == TokenType.PRIMITIVE_TYPE) {
                isCollectingDeclarations = true;
                declarationType = token.getTokenString();
            }

            if(token.getTokenType() == TokenType.SEMI_COLON)
                isCollectingDeclarations = false;
        }
    }

    private void addToDeclarationMap(String tokenType, Token token) {
        if(declarationsMap.get(tokenType) == null)
            declarationsMap.put(tokenType, new ArrayList<>());
        declarationsMap.get(tokenType).add(token);
    }


    private void verifyNoDeclarationError(Token tokenToVerify) throws TypeErrorException {
        if(hasNotBeenDeclared(tokenToVerify))
            throw new TypeErrorException("Identifier '" + tokenToVerify.getTokenString() + "' has not been declared.", tokenToVerify.getLine());
    }

    private boolean hasNotBeenDeclared(Token tokenToVerify) {
        return !hasBeenDeclared(tokenToVerify);
    }

    private boolean hasBeenDeclared(Token tokenToVerify) {
        boolean isDeclared = false;
        Token currentToken, prevToken;
        for(int i = 0; i < this.parsedTokens.size(); i++) {
            currentToken = this.parsedTokens.get(i);
            prevToken = this.parsedTokens.get(i == 0? i : i - 1);
            if(tokenToVerify.getTokenString().equals(currentToken.getTokenString())) {
                if (isDeclarationStatement(prevToken, currentToken)) {
                    isDeclared = true;
                    break;
                }
                else if (isInListDeclaration(prevToken, currentToken)) {
                    isDeclared = true;
                    break;
                }
            }
        }
        return  isDeclared;
    }

    private boolean isInListDeclaration (Token prevToken, Token currentToken) {
        boolean isInListDeclaration = true;
        isInListDeclaration = isInListDeclaration && (currentToken.getTokenType() == TokenType.IDENTIFIER);
        isInListDeclaration = isInListDeclaration && (prevToken.getTokenType() == TokenType.COMMA);
        if(isInListDeclaration) {
            Token typeToken = getIdentifierTokenDataType(currentToken);
            if(typeToken.isEmptyToken())
                isInListDeclaration = false;
        }
        return isInListDeclaration;
    }

    private Token getIdentifierTokenDataType(Token identifierToken) {
        int tokenIndex = this.parsedTokens.indexOf(identifierToken);
        int prevTokenIndex = tokenIndex == 0? 0 : tokenIndex - 1;

        Token typeToken = Token.createEmptyToken();
        Token prevToIdToken = this.parsedTokens.get(prevTokenIndex);
        Token iToken;

        if(prevToIdToken.getTokenType() == TokenType.PRIMITIVE_TYPE) {
            tokenIndex = -1; //:: Skip over the iteration
            typeToken = prevToIdToken;
        }

        for(int i = tokenIndex; i >= 0; i--) {
            iToken = this.parsedTokens.get(i);
            if(iToken.getTokenType() == TokenType.SEMI_COLON)
                break;
            else if (iToken.getTokenType() == TokenType.PRIMITIVE_TYPE) {
                typeToken = iToken;
                break;
            }
        }

        return typeToken;
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
        Token prevToken;
        for(int i = 0; i < this.parsedTokens.size(); i++) {
            iToken = this.parsedTokens.get(i);
            if(iToken.getTokenString().equals(tokenToSeek.getTokenString())) {
                nextToken = this.parsedTokens.get(i == this.parsedTokens.size() - 1? i : i + 1);
                prevToken = this.parsedTokens.get(i == 0? i : i - 1);
                if(isDeclarationStatement(prevToken, iToken))
                    break;
                else if(isInListDeclaration(prevToken, iToken))
                    break;
                else if(isAttributionStatement(iToken, nextToken))
                    break;
                else
                    throw new TypeErrorException("Invalid Symbol for " + iToken.getTokenString(), iToken.getLine());
            }
        }

    }

    private boolean isDeclarationStatement(Token prevToken, Token currentToken) {
        boolean isDeclaration = true;
        isDeclaration = isDeclaration && (currentToken.getTokenType() == TokenType.IDENTIFIER);
        isDeclaration = isDeclaration && (prevToken.getTokenType() == TokenType.PRIMITIVE_TYPE);
        return  isDeclaration;
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

    private void generateSymbolTable() {
        Token iToken;
        for(int i = 0; i < this.parsedTokens.size(); i++) {
            iToken = this.parsedTokens.get(i);
            if(iToken.getTokenType() == TokenType.IDENTIFIER)
                createSymbol(iToken, i);
        }
    }

    private void createSymbol(Token token, int position) {
        Token prevToken = this.parsedTokens.get(position == 0? position: position - 1);
        Token typeToken = getIdentifierTokenDataType(token);
        Symbol symbol = new Symbol(token.getTokenString());
        if(isDeclarationStatement(prevToken, token) || isInListDeclaration(prevToken, token)) {
            symbol.category = "variavel";
            symbol.type = typeToken.getTokenString();
            symbol.memoryStructure = "simples";
            symbol.level = getTokenLevel(token);
            this.symbolTable.add(symbol);
        }
    }

    private String getTokenLevel(Token token) {
        String levelString = "0.";
        int lastLineNumber = 0;
        for(Token iToken : this.parsedTokens) {
            if(iToken.getTokenString().equals(token.getTokenString())){
                levelString += (iToken.getLine() - lastLineNumber);
                break;
            }

            if(iToken.getTokenType() == TokenType.DO) {
                levelString += (iToken.getLine() - lastLineNumber) + ".";
                lastLineNumber = iToken.getLine();
            }
        }
        return levelString;
    }

}
