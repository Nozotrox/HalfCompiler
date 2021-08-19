import Analyzers.*;
import Analyzers.Lexical.LexicalAnalyzer;
import Analyzers.Lexical.Token;
import Analyzers.Syntax.SyntaxAnalisis;
import Exceptions.CustomException;
import Exceptions.TypeErrorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String getInputFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        String content = "";
        while (sc.hasNextLine())
            content += " " + sc.nextLine();
        return content;
    }

    public static void main(String[] args){

        //::: Ainda liguei o processo de tokenizacao com o de parsint. Este parsing esta incompleto.
        // Reconhece domente doWhiles com um _ entre o corpo. Todos tokens devem ser separados por espaco.
        try {
            File contentFile = new File("D:\\Feliciano\\ISCTEM\\Quarto_Ano\\Primeiro_Semestre\\Compiladores\\CODE\\src\\dowhile.txt");
            //:: Analise Lexica
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(contentFile);
            lexicalAnalyzer.analyze();
//            lexicalAnalyzer.printSymbolTable();
//            lexicalAnalyzer.printLexicalTable();

            //:: Analise Sintatica
            SyntaxAnalisis syntaxAnalisis = new SyntaxAnalisis(lexicalAnalyzer.getParsedTokens());
            syntaxAnalisis.makeAnalysis();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TypeErrorException e) {
            System.out.println(((CustomException) e).getErrorMessage());
        }

    }
}
