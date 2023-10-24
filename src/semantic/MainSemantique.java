package semantic;

import semantic.AnalyseurSemantique;

import java.io.File;

public class MainSemantique {
    public static void main(String[] args, File fileACompiler) throws Exception {
        AnalyseurSemantique semantique = new AnalyseurSemantique(fileACompiler);
        semantique.analyseSemantique();
    }
}
