import Analyzers.Lexical.LexicalAnalyzer;
import Analyzers.Semantics.SemanticsAnalyzer;
import Analyzers.Syntax.SyntaxAnalyzer;
import Exceptions.CustomException;
import Exceptions.SemanticsException;
import Exceptions.LexicalException;
import Exceptions.SyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args){

        try {
            File contentFile = new File("D:\\Feliciano\\ISCTEM\\Quarto_Ano\\Primeiro_Semestre\\Compiladores\\CODE\\src\\dowhile.txt");
            //:: Analise Lexica
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(contentFile);
            lexicalAnalyzer.analyze();

            //:: Analise Sintatica
            SyntaxAnalyzer syntaxAnalisis = new SyntaxAnalyzer(lexicalAnalyzer.getParsedTokens(), false);
            syntaxAnalisis.analyze();

            //:: Analise Semantica
            SemanticsAnalyzer semanticsAnalyzer = new SemanticsAnalyzer(lexicalAnalyzer.getParsedTokens(), lexicalAnalyzer.getDeclaraionsMap(), lexicalAnalyzer.getSymbolTable());
            semanticsAnalyzer.analyze();

            System.out.println("\n::: DECLARACAO DO WHILE VALIDA");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexicalException e) {
            System.out.println("\n" + ((CustomException) e).getErrorMessage());
        } catch (SemanticsException e) {
            System.out.println("\n" + ((CustomException) e).getErrorMessage());
        } catch (SyntaxException e) {
            System.out.println("\n" + ((CustomException) e).getErrorMessage());
        }

    }
}
