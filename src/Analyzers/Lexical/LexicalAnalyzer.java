package Analyzers.Lexical;

import Exceptions.LexicalException;

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

    public HashMap<String ,ArrayList<Token>> getDeclaraionsMap() {
        return this.declarationsMap;
    }

    public ArrayList<Symbol> getSymbolTable() {
        return symbolTable;
    }

    public void analyze() throws IOException, LexicalException {
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

    private void verifyLexicalErrors() throws LexicalException {
        verifyIdentifierErrors();
        verifyMalformedElements();
    }

    private void verifyIdentifierErrors() throws LexicalException {
        collectAllDeclarations();
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.IDENTIFIER) {
                verifyInvalidIdentifierSymbol(tokenToAnalyze);
            }
        }
    }

    private void collectAllDeclarations() {
        boolean isCollectingDeclarations = false;
        boolean isAttribution = false;
        String declarationType = "";
        for(Token token : this.parsedTokens) {
            if(token.getTokenType() == TokenType.EQUAL) {
                isAttribution = true;
                continue;
            }

            if(isAttribution) {
                isAttribution = false;
                continue;
            }

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




    private void verifyInvalidIdentifierSymbol (Token tokenToSeek) throws LexicalException {
        if(hasNotBeenDeclared(tokenToSeek))
            throw new LexicalException("Invalid Symbol for " + tokenToSeek.getTokenString(), tokenToSeek.getLine());
    }

    private void verifyMalformedElements() throws LexicalException {
        for(Token tokenToAnalyze : this.parsedTokens) {
            if(tokenToAnalyze.getTokenType() == TokenType.CHARACTER_CONSTANT)
                verifyIfCharacterIsMalformed(tokenToAnalyze);
        }
    }

    private void verifyIfCharacterIsMalformed(Token tokenToSeek) throws LexicalException {
        if(tokenToSeek.getTokenString().length() != 1)
            throw new LexicalException("Malformed character literal " + tokenToSeek.getTokenString(), tokenToSeek.getLine());
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
            symbol.level = LexicalAnalyzer.getTokenLevel(token, this.parsedTokens);
            this.symbolTable.add(symbol);
        }
    }

    public static String getTokenLevel(Token token, ArrayList<Token> parsedTokens) {
        String levelString = "0.";
        boolean toAdd = false;
        int lastLineNumber = 0;
        for(Token iToken : parsedTokens) {
            if(iToken == token){
                levelString += toAdd? lastLineNumber + (iToken.getLine() - lastLineNumber):iToken.getLine() - lastLineNumber;
                break;
            }

            if(iToken.getTokenType() == TokenType.DO) {
                levelString += (iToken.getLine() - lastLineNumber) + ".";
                lastLineNumber = iToken.getLine();
                toAdd = false;
            }
            if (iToken.getTokenType() == TokenType.WHILE) {
                levelString = levelString.substring(0, levelString.length() - 1);
                int lastLevelIndex = levelString.lastIndexOf(".");
                levelString = levelString.substring(0, lastLevelIndex + 1);
                toAdd = true;
            }
        }
        return levelString;
    }

}
