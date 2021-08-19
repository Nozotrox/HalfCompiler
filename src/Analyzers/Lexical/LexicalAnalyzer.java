package Analyzers.Lexical;

import Analyzers.TokenType;
import Exceptions.CustomException;
import Exceptions.TypeErrorException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    private boolean hasNotBeenDeclared(Token tokenToVerify) {
        return !hasBeenDeclared(tokenToVerify);
    }

    private boolean hasBeenDeclared(Token tokenToVerify) {
        boolean isDeclared = false;
        for(ArrayList<Token> declaredTokens : this.declarationsMap.values()) {
            for(Token token : declaredTokens) {
                isDeclared = token.getTokenString().equals(tokenToVerify.getTokenString());
                isDeclared = isDeclared && (token.getLine() <= tokenToVerify.getLine());
                if(isDeclared)
                    break;
            }
            if(isDeclared)
                break;
        }
        return isDeclared;
    }


    private String getIdentifierTokenDataType(Token identifierToken) {
        String tokenType = "";
        for(String tType: this.declarationsMap.keySet()) {
            if(this.declarationsMap.get(tType).contains(identifierToken))
                tokenType = tType;
        }
        return tokenType;
    }


    private void verifyMultipleDeclarationsError(Token tokenToSeek) throws TypeErrorException {
        int declarationsCount = 0, lastDeclarationLine = 0;
        for (ArrayList<Token> declaredTokens: this.declarationsMap.values()) {
            for(Token token : declaredTokens)
                if(token.getTokenString().equals(tokenToSeek.getTokenString())) {
                    lastDeclarationLine = token.getLine();
                    declarationsCount++;
                }
        }
        if(declarationsCount > 1)
            throw new TypeErrorException("Identifier '" + tokenToSeek.getTokenString() + "' has already been declared.", lastDeclarationLine);

    }

    private boolean areTokensInSameScope(Token token1, Token token2) {
        String levelString1 = getTokenLevel(token1);
        String levelString2 = getTokenLevel(token2);
        String scope1 = levelString1.substring(0, levelString1.lastIndexOf("."));
        String scope2 = levelString2.substring(0, levelString2.lastIndexOf("."));
        return scope1.equals(scope2);
    }

    private void verifyInvalidIdentifierSymbol (Token tokenToSeek) throws TypeErrorException {
        if(hasNotBeenDeclared(tokenToSeek))
            throw new TypeErrorException("Invalid Symbol for " + tokenToSeek.getTokenString(), tokenToSeek.getLine());
    }

    private void verifyMalformedElements() throws TypeErrorException {
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.CHARACTER_CONSTANT)
                verifyIfCharacterIsMalformed(tokenToAnalyze);
        }
    }

    private void verifyIfCharacterIsMalformed(Token tokenToSeek) throws TypeErrorException {
        if(tokenToSeek.getTokenString().length() != 1)
            throw new TypeErrorException("Malformed character literal " + tokenToSeek.getTokenString(), tokenToSeek.getLine());
    }

    private void generateSymbolTable() {
        for(ArrayList<Token> declaredTokens : this.declarationsMap.values()) {
            for(Token iToken: declaredTokens)
                createSymbol(iToken);
        }
    }

    private void createSymbol(Token token) {
        String typeTokenString = getIdentifierTokenDataType(token);
        Symbol symbol = new Symbol(token.getTokenString());
        if(hasBeenDeclared(token)) {
            symbol.category = "variavel";
            symbol.type = typeTokenString;
            symbol.memoryStructure = "simples";
            symbol.level = getTokenLevel(token);
            this.symbolTable.add(symbol);
        }
    }

    private String getTokenLevel(Token token) {
        String levelString = "0.";
        int lastLineNumber = 0;
        for(Token iToken : this.parsedTokens) {
            if(iToken == token){
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
