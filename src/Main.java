import Analyzer.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        String empty = AutomataStack.DEFAULT_EMPTY_STACK_SYMBOL;
        StackOperation maintain = StackOperation.MAINTAIN;
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
        PDTransintion tDo1 = new PDTransintion("do", empty, maintain, qa);
        PDTransintion tDo2 = new PDTransintion("do", empty, StackOperation.INSERT, qa);
        PDTransintion tDo3 = new PDTransintion("do", "do", StackOperation.INSERT, qa);

        PDTransintion tOcb1 = new PDTransintion("{", empty, maintain, q1);
        PDTransintion tOcb2 = new PDTransintion("{", "do", maintain, q1);

        PDTransintion tCcb1 = new PDTransintion("}", empty, maintain, qc);
        PDTransintion tCcb2 = new PDTransintion("}", "do", maintain, qc);

        PDTransintion tWhile1 = new PDTransintion("while", empty, maintain, qd);
        PDTransintion tWhile2 = new PDTransintion("while", "do", maintain, qd);

        PDTransintion tOb1 = new PDTransintion("(", empty, maintain, q2);
        PDTransintion tOb2 = new PDTransintion("(", "do", maintain, q2);

        PDTransintion tTrue1 = new PDTransintion("true", empty, maintain, q3);
        PDTransintion tTrue2 = new PDTransintion("true", "do", maintain, q3);
        PDTransintion tFalse1 = new PDTransintion("false", empty, maintain, q3);
        PDTransintion tFalse2 = new PDTransintion("false", "do", maintain, q3);


        PDTransintion tCb1 = new PDTransintion(")", empty, maintain, qe);
        PDTransintion tCb2 = new PDTransintion(")", "do", maintain, qe);

        PDTransintion tSc1 = new PDTransintion(";", "do", StackOperation.ERASE, q1);
        PDTransintion tSc2 = new PDTransintion(";", empty, maintain, ef);

        PDTransintion tType1 = new PDTransintion("type_tkn", empty, maintain, qf);
        PDTransintion tType2 = new PDTransintion("type_tkn", "do", maintain, qf);

        PDTransintion tIdentifier1 = new PDTransintion("id_tkn", empty, maintain, qg);
        PDTransintion tIdentifier2 = new PDTransintion("id_tkn", "do", maintain, qg);

        PDTransintion tEquals1 = new PDTransintion("=", empty, maintain, qh);
        PDTransintion tEquals2 = new PDTransintion("=", "do", maintain, qh);

        PDTransintion tCharLit1 = new PDTransintion("charLit_tkn", empty, maintain, qi);
        PDTransintion tCharLit2 = new PDTransintion("charLit_tkn", "do", maintain, qi);

        PDTransintion tBoolean1 = new PDTransintion("boolean_tkn", empty, maintain, qi);
        PDTransintion tBoolean2 = new PDTransintion("boolean_tkn", "do", maintain, qi);

        PDTransintion tSemiColon1 =  new PDTransintion(";", empty, maintain, q1);
        PDTransintion tSemiColon2 =  new PDTransintion(";", "do", maintain, q1);

        //::: ADDING TRANSITIONS TO STATE
        q0.addTransition(tDo1);

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

        //::: Ainda liguei o processo de tokenizacao com o de parsint. Este parsing esta incompleto.
        // Reconhece domente doWhiles com um _ entre o corpo. Todos tokens devem ser separados por espaco.
        try {
            File contentFile = new File("D:\\Feliciano\\ISCTEM\\Quarto_Ano\\Primeiro_Semestre\\Compiladores\\CODE\\src\\dowhile.txt");
            //:: Analise Lexica
            Tokenizer tk = new Tokenizer(contentFile);
            tk.parseTokens();
//            ArrayList<String> tokens = tk.parseTokensAsString();
            tk.printParsedTokens();
            //:: Analise Sintatica
//            PushDownAutomata pda = new PushDownAutomata(q0, tokens);
//            pda.validateInput();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
