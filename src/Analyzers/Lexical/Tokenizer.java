package Analyzers.Lexical;

import Exceptions.LexicalException;

import java.io.*;
import java.util.ArrayList;

class Tokenizer {
    private  ArrayList<Token> tokens = new ArrayList<>();
    private File contentFile;

    public Tokenizer (File contentFile) {
        this.contentFile = contentFile;
    }

    public void printParsedTokens() {
        Token token;
        for(Object obj: tokens) {
            token = (Token) obj;
            System.out.printf(" %-30s  %-30s  %10s \n", token.getTokenType(),token.getTokenString(), token.getLine());
        }
    }

    public void parseTokens( ) throws IOException, LexicalException {
        StreamTokenizer streamTokenizer = getConfiguredTokenizer();
        this.tokens = tokenize(streamTokenizer);
    }

    private StreamTokenizer getConfiguredTokenizer() throws IOException {
        FileReader fr = new FileReader(this.contentFile);
        StreamTokenizer streamTokenizer = new StreamTokenizer(fr);
        streamTokenizer.eolIsSignificant(true);
        return streamTokenizer;
    }

    private ArrayList<Token>  tokenize(StreamTokenizer streamTokenizer) throws IOException, LexicalException {
        ArrayList<Token> tokens = new ArrayList<>();
        int currentTokenCode = streamTokenizer.nextToken();
        char currentToken;
        Token token, linkedToken;
        int line = 1;

        while(currentTokenCode != StreamTokenizer.TT_EOF) {
            if(streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                token = Token.buildWordToken(streamTokenizer.sval, line);
                tokens.add(token);
            } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
//                tokens.add(streamTokenizer.nval);
            } else if(streamTokenizer.ttype == StreamTokenizer.TT_EOL) {
                line++;
            } else {
                currentToken = (char) currentTokenCode;
                token = Token.buildOrdinaryToken(currentToken, line);
                tokens.add(token);
                if(containsToken(streamTokenizer)) {
                    tokens.remove(tokens.size() - 1);
                    linkedToken = Token.buildTokenLinkedWithToken(token, streamTokenizer.sval, line);
                    tokens.add(linkedToken);
                }
            }
            currentTokenCode = streamTokenizer.nextToken();
        }
        return tokens;
    }

    private boolean containsToken(StreamTokenizer streamTokenizer) {
        return (streamTokenizer.sval != null);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }


}
