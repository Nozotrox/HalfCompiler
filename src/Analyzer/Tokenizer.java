package Analyzer;

import java.io.*;
import java.util.ArrayList;

public class Tokenizer {
    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '\"';
    private static ArrayList<Object> tokens = new ArrayList<>();

    public static void main(String[] args){
        try {
            FileReader fr = new FileReader(new File("D:\\Feliciano\\ISCTEM\\Quarto_Ano\\Primeiro_Semestre\\Compiladores\\CODE\\src\\code.txt"));
            tokenize(fr);
        } catch (FileNotFoundException e) {
            System.out.println("COULD NOT FIND FILE.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("TYPE ERROR.");
            e.printStackTrace();
        }
    }

    private static void tokenize(FileReader fr) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(fr);

        int currentToken = streamTokenizer.nextToken();
        while(currentToken != StreamTokenizer.TT_EOF) {
            if(streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                tokens.add(streamTokenizer.sval);
                System.out.println("WORD: " + streamTokenizer.sval);
            } else if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                tokens.add(streamTokenizer.nval);
                System.out.println("NUMBER: " + streamTokenizer.nval);
            } else {
                tokens.add((char) currentToken);
                System.out.println("ORDINARY: " + ((char) currentToken));
            }
            currentToken = streamTokenizer.nextToken();
        }
    }

}
