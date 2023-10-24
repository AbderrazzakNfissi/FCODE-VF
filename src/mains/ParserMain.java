package mains;

import parser.AnalyseurSyntaxique;

import java.io.File;

public class ParserMain {

        public static void main(String[] args, File fileACompiler) throws Exception{

            AnalyseurSyntaxique as = new AnalyseurSyntaxique(fileACompiler);
            as.analyseSyntax();
            if(as.isCorrect){
                System.out.println("<h2 style='color:green' align='center'>le  programme est correcte syntaxiquement!</h2>");
            }else{

            }

    }
}
