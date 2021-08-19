package Analyzers.Lexical;

import Exceptions.LexicalException;

import java.util.Arrays;
import java.util.List;

public class Token {
    private static final List<String> reservedWords =  Arrays.asList("do", "while");
    private static final List<String> dataTypes = Arrays.asList("char", "boolean");
    private static final List<String> literalConstants = Arrays.asList("true", "false");


    private final String tokenString;
    private final TokenType tokenType;
    private final int line;

    public Token(String tokenString, TokenType tokenType, int line) {
        this.tokenString = tokenString;
        this.tokenType = tokenType;
        this.line = line;
    }

    public String getTokenString() {
        return this.tokenString;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getLine() {
        return line;
    }

    public void printToken() {
        System.out.printf("%-20s %-70s %3d\n", this.tokenString, this.tokenType.getDescription(), this.getLine());
    }

    public boolean isEmptyToken() {
        boolean isEmptyToken = true;
        isEmptyToken = isEmptyToken && this.tokenString.isEmpty();
        isEmptyToken = isEmptyToken && (this.tokenType == null);
        isEmptyToken = isEmptyToken && (this.line == -1);
        return isEmptyToken;
    }

    public static Token createEmptyToken() {
        return new Token("", null, -1);
    }

    public static Token buildWordToken(String tokenString, int line) {
        Token token;
        if(canBeIdentifier(tokenString))
            token = new Token(tokenString, TokenType.IDENTIFIER, line);
        else {
            TokenType tokenType = getSpecialWordTokenType(tokenString);
            token = new Token(tokenString, tokenType, line);
        }
        return token;
    }

    public static Token buildOrdinaryToken(char tokenChar, int line) throws LexicalException {
        TokenType tokenType = getOrdinaryTokenType(tokenChar, line);
        Token token = new Token(tokenChar + "", tokenType, line);
        return token;
    }

    public static Token buildTokenLinkedWithToken(Token token, String linkedTokenString, int line) throws LexicalException {
        TokenType tokenType = getLinkedTokenTypeOf(token, linkedTokenString);
        Token linkedToken = new Token(linkedTokenString, tokenType, line);
        return linkedToken;
    }

    private static boolean canBeIdentifier(String tokenString) {
        boolean isOfUnusableWords = false;
        isOfUnusableWords = isOfUnusableWords || Token.reservedWords.contains(tokenString);
        isOfUnusableWords = isOfUnusableWords || Token.dataTypes.contains(tokenString);
        isOfUnusableWords = isOfUnusableWords || Token.literalConstants.contains(tokenString);
        return !isOfUnusableWords;
    }

    private static TokenType getSpecialWordTokenType(String tokenString) {
        TokenType tokenType;
        switch (tokenString) {
            case "do":; tokenType = TokenType.DO;break;
            case "while": tokenType = TokenType.WHILE;break;
            case "char":; //:: NULL STATEMENT
            case "boolean": tokenType = TokenType.PRIMITIVE_TYPE;break;
            case "true":; //:: NULL STATEMENT
            case "false": tokenType = TokenType.BOOLEAN_CONSTANT;break;
            default:
                throw new IllegalArgumentException("E: Could not identify " + tokenString + " .");
        }
        return  tokenType;
    }

    private static TokenType getOrdinaryTokenType(char tokenChar, int line) throws LexicalException {
        TokenType tokenType;
        switch (tokenChar) {
            case '\'': tokenType = TokenType.SINGLE_QUOTE;break;
//            case '\"': tokenType = TokenType.DOUBLE_QUOTE;break;
            case ';': tokenType = TokenType.SEMI_COLON;break;
            case '{': tokenType = TokenType.OPEN_CURLY_BRACKET;break;
            case '}': tokenType = TokenType.CLOSE_CURLY_BRACKET;break;
            case '(': tokenType = TokenType.OPEN_BRACKET;break;
            case ')': tokenType = TokenType.CLOSE_BRACKET;break;
            case '=': tokenType = TokenType.EQUAL;break;
            case ',': tokenType = TokenType.COMMA;break;
            default:
                throw new LexicalException("Could not identify " + tokenChar + " .", line);
        }
        return tokenType;
    }

    private static TokenType getLinkedTokenTypeOf(Token token, String linkedTokenString) throws LexicalException {
        TokenType tokenType;
        switch (token.getTokenType()) {
            case SINGLE_QUOTE: tokenType = TokenType.CHARACTER_CONSTANT;break;
            case DOUBLE_QUOTE: tokenType = TokenType.STRING_CONSTANT;break;
            default:
                throw new LexicalException("Could not identify " + linkedTokenString + " .", token.getLine());
        }
        return tokenType;
    }


}
