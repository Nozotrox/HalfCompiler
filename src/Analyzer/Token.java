package Analyzer;

import java.util.ArrayList;
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

    public static Token buildOrdinaryToken(char tokenChar, int line) {
        TokenType tokenType = getOrdinaryTokenType(tokenChar);
        Token token = new Token(tokenChar + "", tokenType, line);
        return token;
    }

    public static Token buildTokenLinkedWithToken(Token token, String linkedTokenString, int line) {
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
            case "do":; //:: NULL STATEMENT
            case "while": tokenType = TokenType.RESERVED_WORD;break;
            case "char":; //:: NULL STATEMENT
            case "boolean": tokenType = TokenType.PRIMITIVE_TYPE;break;
            case "true":; //:: NULL STATEMENT
            case "false": tokenType = TokenType.BOOLEAN_CONSTANT;break;
            default:
                throw new IllegalArgumentException("TypeError: Could not identify " + tokenString + " .");
        }
        return  tokenType;
    }

    private static TokenType getOrdinaryTokenType(char tokenChar) {
        TokenType tokenType;
        switch (tokenChar) {
            case '\'': tokenType = TokenType.SINGLE_QUOTE;break;
            case '\"': tokenType = TokenType.DOUBLE_QUOTE;break;
            case ';': tokenType = TokenType.SEMI_COLON;break;
            case '{': tokenType = TokenType.OPEN_CURLY_BRACKET;break;
            case '}': tokenType = TokenType.CLOSE_CURLY_BRACKET;break;
            case '(': tokenType = TokenType.OPEN_BRACKET;break;
            case ')': tokenType = TokenType.CLOSE_BRACKET;break;
            case '=': tokenType = TokenType.EQUAL;break;
            default:
                throw new IllegalArgumentException("TypeError: Could not identify " + tokenChar + " .");
        }
        return tokenType;
    }

    private static TokenType getLinkedTokenTypeOf(Token token, String linkedTokenString) {
        TokenType tokenType;
        switch (token.getTokenType()) {
            case SINGLE_QUOTE: tokenType = TokenType.CHARACTER_CONSTANT;break;
            case DOUBLE_QUOTE: tokenType = TokenType.STRING_CONSTANT;break;
            default:
                throw new IllegalArgumentException("TypeError: Could not identify " + linkedTokenString + " .");
        }
        return tokenType;
    }
}
