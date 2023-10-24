package semantic;

import io.github.cdimascio.dotenv.Dotenv;
import lexer.AnalyseurLexical;
import lexer.CODES_LEX;
import lexer.TSym_Cour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class AnalyseurSemantique {
    private ArrayList<CODES_LEX> listLexical = new ArrayList<>();
    private ArrayList<String> tokensName =  new ArrayList<>();
    private HashMap<String,String> typeFunctionOrVar = new HashMap<>();
    private HashMap<String, CODES_LEX> typeSpecifierID = new HashMap<>();
    private HashMap<String,String> valueIDToken = new HashMap<>();

    private CODES_LEX CODE_LEX_Cour;

    private int index = 0;
    int scope =0;
    private File fileACompiler;

    AnalyseurSemantique(File fileACompiler) throws Exception{
        this.fileACompiler = fileACompiler;
        analyseurLexical();
    }

    void analyseurLexical() throws Exception{

//        File file = new File("C:\\Users\\AyouByte\\Desktop\\projet compila\\last dial last\\FCODE-cloned\\fcode.fc");
//        File file = new File("C:\\Users\\HP\\Desktop\\S3\\projetCompilateur\\compilation\\fcode.fc");

        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
        File file= new File("/home/ayoubyte/code.fc");
        FileReader fr = new FileReader(fileACompiler);
        BufferedReader bf = new BufferedReader(fr);
        List<Character> charList = new ArrayList<>();
        int c;
        String line = "";
        String code = "";

        while((line = bf.readLine()) != null) {
            code += line + " ";
        }
        for(int i=0;i<code.length();i++){
            charList.add(code.charAt(i));
        }
        AnalyseurLexical al = new AnalyseurLexical(charList);
        al.Lire_Car();
        this.listLexical = new ArrayList<>();
        this.tokensName = new ArrayList<>();
        while (al.getCour()<charList.size()-1){
            TSym_Cour sym_cour = al.sym_suivant();
            if(sym_cour.CODE != CODES_LEX.COMMENT_TOKEN){
                tokensName.add(sym_cour.nom);
                listLexical.add( sym_cour.CODE);
            }
            al.Lire_Car();
            while((al.Car_Cour==' ' || al.Car_Cour=='\n' || al.Car_Cour=='\t') && al.getCour()<charList.size()) {
                al.Lire_Car();
            }
        }
        fr.close();
    }
    public void analyseSemantique(){
        boolean s = test_functions(listLexical,tokensName);
        boolean b = declaration_var(listLexical,tokensName);
        if( s && b ){
            Set<String> keys = typeSpecifierID.keySet();
            for (String key : keys) {
                CODES_LEX value = typeSpecifierID.get(key);
                String[] types = key.split(":");
                System.out.println(types[0] + ": " + value);
            }
        }
    }
    public boolean declaration_var (ArrayList listLexical , ArrayList tokensName) {
        listLexical=this.listLexical;
        tokensName=this.tokensName;
        index=0;
        String nom;
        while (index <= listLexical.size() - 1) {
            if(listLexical.get(index) == CODES_LEX.FIN_TOKEN) scope++;
                if (listLexical.get(index) == CODES_LEX.ID_TOKEN) {
                    nom = (String) tokensName.get(index);
                    nom+=":"+scope;
                    index++;
                    if (listLexical.get(index) == CODES_LEX.DEUX_POINTS_TOKEN) {
                        index++;
                        if (!typeFunctionOrVar.containsKey(nom)) {
                            typeFunctionOrVar.put(nom,"variable");
                            typeSpecifierID.put(nom, (CODES_LEX) listLexical.get(index++));
                        }else {
                            System.err.println("id : " +nom+" exists with same name");
                            return false;
                        }
                    }
                }
            index++;
        }
        return true;
    }

    public boolean test_functions(ArrayList listLexical,ArrayList tokensName){
        listLexical=this.listLexical;
        tokensName=this.tokensName;
        String nom,value;
        index=0;
        while(index<= listLexical.size()-1){
            if( listLexical.get(index) == CODES_LEX.FONCTION_TOKEN ){
                index++;
                nom= (String) tokensName.get(index);
                nom+=":f";
                if(!typeFunctionOrVar.containsKey(nom)){
                    typeFunctionOrVar.put(nom,"fonction");
                    while(listLexical.get(index)!= CODES_LEX.PF_TOKEN) index++;
                    index+=2;
                    typeSpecifierID.put(nom, (CODES_LEX) listLexical.get(index));
                    while(listLexical.get(index)!= CODES_LEX.RETOURNER_TOKEN) index++;
                    index++;
                    value =(String) tokensName.get(index);
                    if(value.equals(""+ CODES_LEX.PV_TOKEN)) valueIDToken.put(nom,null);
                    else valueIDToken.put(nom,value);
                }else {
                    String[] noms =nom.split(":");
                    System.err.println("fonction " +noms[0]+" exists with same name");
                    System.exit(-1);
                    return false;
                }
            }
            index++;
        }
        return true;
    }
}
