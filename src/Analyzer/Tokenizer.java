package Analyzer;

import java.io.*;
import java.util.ArrayList;

public class Tokenizer {
    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '\"';
    private  ArrayList<Object> tokens = new ArrayList<>();
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

    public void parseTokens( ) throws IOException {
        StreamTokenizer streamTokenizer = getConfiguredTokenizer();
        tokens = tokenize(streamTokenizer);
    }

    private StreamTokenizer getConfiguredTokenizer() throws FileNotFoundException {
        FileReader fr = new FileReader(this.contentFile);
        StreamTokenizer streamTokenizer = new StreamTokenizer(fr);
        streamTokenizer.eolIsSignificant(true);
        return streamTokenizer;
    }

    private ArrayList<Object>  tokenize(StreamTokenizer streamTokenizer) throws IOException {
        ArrayList<Object> tokens = new ArrayList<>();
        int currentTokenCode = streamTokenizer.nextToken();
        char currentToken;
        Token token, linkedToken;
        int line = 1;

        while(currentTokenCode != StreamTokenizer.TT_EOF) {
            if(streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                token = Token.buildWordToken(streamTokenizer.sval, line);
                tokens.add(token);
            } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                tokens.add(streamTokenizer.nval);
            } else if(streamTokenizer.ttype == StreamTokenizer.TT_EOL) {
                line++;
            } else {
                currentToken = (char) currentTokenCode;
                token = Token.buildOrdinaryToken(currentToken, line);
                tokens.add(token);
                if(containsToken(streamTokenizer)) {
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

    public ArrayList<Object> getTokens() {
        return tokens;
    }


}
