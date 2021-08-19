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
//            syntaxAnalisis.makeAnalysis();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TypeErrorException e) {
            System.out.println(((CustomException) e).getErrorMessage());
        }

    }
}

/*String empty = AutomataStack.DEFAULT_EMPTY_STACK_SYMBOL;
        StackOperation maintain = StackOperation.MAINTAIN;
        String z1 = "z1";
        //:: MAIN STATES
        State q0 = new State("q0", false, true);
        State q1 = new State("q1");
        State q2 = new State("q2");
        State q3 = new State("q3");
        State ef = new State("ef", true, false);
        //:: INTERMEDIARY STATES
        State qa = new State("qa");
        State qc = new State("qc");
        State qd = new State("qd");
        State qe = new State("qe");
        State qf = new State("qf");
        State qg = new State("qg");
        State qh = new State("qh");
        State qi = new State("qi");

        //::: TRANSITIONS
        PDTransintion tDo1 = new PDTransintion(TokenType.DO, empty, maintain, qa);
        PDTransintion tDo2 = new PDTransintion(TokenType.DO, empty, StackOperation.INSERT, qa);
        PDTransintion tDo3 = new PDTransintion(TokenType.DO, "do", StackOperation.INSERT, qa);

        PDTransintion tOcb1 = new PDTransintion(TokenType.OPEN_CURLY_BRACKET, empty, maintain, q1);
        PDTransintion tOcb2 = new PDTransintion(TokenType.OPEN_CURLY_BRACKET, "do", maintain, q1);

        PDTransintion tCcb1 = new PDTransintion(TokenType.CLOSE_CURLY_BRACKET, empty, maintain, qc);
        PDTransintion tCcb2 = new PDTransintion(TokenType.CLOSE_CURLY_BRACKET, "do", maintain, qc);

        PDTransintion tWhile1 = new PDTransintion(TokenType.WHILE, empty, maintain, qd);
        PDTransintion tWhile2 = new PDTransintion(TokenType.WHILE, "do", maintain, qd);

        PDTransintion tOb1 = new PDTransintion(TokenType.OPEN_BRACKET, empty, maintain, q2);
        PDTransintion tOb2 = new PDTransintion(TokenType.OPEN_BRACKET, "do", maintain, q2);

        PDTransintion tTrue1 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, empty, maintain, q3);
        PDTransintion tTrue2 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, "do", maintain, q3);
        PDTransintion tFalse1 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, empty, maintain, q3);
        PDTransintion tFalse2 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, "do", maintain, q3);


        PDTransintion tCb1 = new PDTransintion(TokenType.CLOSE_BRACKET, empty, maintain, qe);
        PDTransintion tCb2 = new PDTransintion(TokenType.CLOSE_BRACKET, "do", maintain, qe);

        PDTransintion tSc1 = new PDTransintion(TokenType.SEMI_COLON, "do", StackOperation.ERASE, q1);
        PDTransintion tSc2 = new PDTransintion(TokenType.SEMI_COLON, empty, maintain, ef);

        PDTransintion tType1 = new PDTransintion(TokenType.PRIMITIVE_TYPE, empty, maintain, qf);
        PDTransintion tType2 = new PDTransintion(TokenType.PRIMITIVE_TYPE, "do", maintain, qf);
        PDTransintion tType3 = new PDTransintion(TokenType.PRIMITIVE_TYPE, empty, StackOperation.INSERT, qf, z1);

        PDTransintion tIdentifier1 = new PDTransintion(TokenType.IDENTIFIER, empty, maintain, qg);
        PDTransintion tIdentifier2 = new PDTransintion(TokenType.IDENTIFIER, "do", maintain, qg);
        PDTransintion tIdentifier3 = new PDTransintion(TokenType.IDENTIFIER, empty, StackOperation.INSERT, qg);

        PDTransintion tEquals1 = new PDTransintion(TokenType.EQUAL, empty, maintain, qh);
        PDTransintion tEquals2 = new PDTransintion(TokenType.EQUAL, "do", maintain, qh);

        PDTransintion tCharLit1 = new PDTransintion(TokenType.CHARACTER_CONSTANT, empty, maintain, qi);
        PDTransintion tCharLit2 = new PDTransintion(TokenType.CHARACTER_CONSTANT, "do", maintain, qi);

        PDTransintion tBoolean1 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, empty, maintain, qi);
        PDTransintion tBoolean2 = new PDTransintion(TokenType.BOOLEAN_CONSTANT, "do", maintain, qi);

        PDTransintion tSemiColon1 =  new PDTransintion(TokenType.SEMI_COLON, empty, maintain, q1);
        PDTransintion tSemiColon2 =  new PDTransintion(TokenType.SEMI_COLON, "do", maintain, q1);


        //::: ADDING TRANSITIONS TO STATE
        q0.addTransition(tDo1);
        q0.addTransition(tType3);
        q0.addTransition(tIdentifier3);


        qa.addTransition(tOcb1);
        qa.addTransition(tOcb2);

        q1.addTransition(tDo2);
        q1.addTransition(tDo3);
        q1.addTransition(tIdentifier1);
        q1.addTransition(tIdentifier2);
        q1.addTransition(tType1);
        q1.addTransition(tType2);
        q1.addTransition(tCcb1);
        q1.addTransition(tCcb2);


        qf.addTransition(tIdentifier1);
            qf.addTransition(tIdentifier2);

            qg.addTransition(tEquals1);
            qg.addTransition(tEquals2);

            qh.addTransition(tCharLit1);
            qh.addTransition(tCharLit2);
            qh.addTransition(tBoolean1);
            qh.addTransition(tBoolean2);

            qi.addTransition(tSemiColon1);
            qi.addTransition(tSemiColon2);


        qc.addTransition(tWhile1);
        qc.addTransition(tWhile2);

        qd.addTransition(tOb1);
        qd.addTransition(tOb2);

        q2.addTransition(tTrue1);
        q2.addTransition(tTrue2);
        q2.addTransition(tFalse1);
        q2.addTransition(tFalse2);

        q3.addTransition(tCb1);
        q3.addTransition(tCb2);

        qe.addTransition(tSc1);
        qe.addTransition(tSc2);
*/