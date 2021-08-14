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
        for(Object token: tokens)
            System.out.println("TOKEN: " + token);
    }

    public void parseTokens( ) throws IOException {
        FileReader fr = new FileReader(this.contentFile);
        StreamTokenizer streamTokenizer = new StreamTokenizer(fr);
        tokens = tokenize(streamTokenizer);
    }

    private ArrayList<Object>  tokenize(StreamTokenizer streamTokenizer) throws IOException {
        ArrayList<Object> tokens = new ArrayList<>();
        int currentToken = streamTokenizer.nextToken();
        while(currentToken != StreamTokenizer.TT_EOF) {
            if(streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                tokens.add(streamTokenizer.sval);
            } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                tokens.add(streamTokenizer.nval);
            } else {
                tokens.add((char) currentToken);
            }
            currentToken = streamTokenizer.nextToken();
        }
        return tokens;
    }

    //::>> #TODO: REMOVE THIS LATER
    public ArrayList<String> parseTokensAsString() throws IOException {
        FileReader fr = new FileReader(this.contentFile);
        StreamTokenizer streamTokenizer = new StreamTokenizer(fr);
        streamTokenizer.wordChars('_', '_');
        tokens = tokenize(streamTokenizer);
        ArrayList<String> sTokens = new ArrayList<>();
        for(Object token: tokens)
            sTokens.add(String.valueOf(token));
        return sTokens;
    }

    public ArrayList<Object> getTokens() {
        return tokens;
    }


}
