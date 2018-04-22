import java.io.*;
import java.util.Scanner;

/**
 * Main class/Lexical Analyzer driver class
 * This class will create an instance of the Lexical Analyzer and allow it to run
 * until the end of the file is reached.
 */

public class Main {
    public static void main(String[] args) throws IOException
    {

        File inputFile;
        FileInputStream fis;

        //Getting input file from designated location
        inputFile = new File("src/in.txt");
        fis = new FileInputStream(inputFile);
        File lexFile = new File("lex.txt");
        File javaOut = new File("javaout.java");
        LexicalAnalyzer lex = new LexicalAnalyzer(fis, lexFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));


        FileWriter fileWriter = new FileWriter(lexFile);

        if (!(inputFile.exists()))
            System.out.println("Error opening input file");
        else
        {
            //Start lexical analyzer
            System.out.println("||||||||||||||||||||||||||||");
            System.out.println("||||  Lexical Analyzer  ||||");
            System.out.println("||||||||||||||||||||||||||||");
            System.out.printf("%-10s%-30s%n", "Token", "Lexeme");
            System.out.printf("%-10s%-30s%n", "-----", "------");
            lex.getChar(fis);
            do {
                lex.lex();
            } while (lex.nextToken != -1 && lex.continueScanning == true && lex.lexLen < 100);

            if(lex.isValid == true)
            {
                System.out.println("|||||||||||||||||||||||");
                System.out.println("||||    PARSER     ||||");
                System.out.println("|||||||||||||||||||||||");
                System.out.println("Checking Syntax Validity . . .");
                Scanner in = new Scanner(new File("lex.txt"));
                Parser parse = new Parser(in);
                parse.checkTopLevel(parse.getNextToken());

                if(parse.isValid == true)
                {
                    System.out.println("|||||||||||||||||||||||");
                    System.out.println("||||  TRANSLATOR   ||||");
                    System.out.println("|||||||||||||||||||||||");
                    System.out.println("Translating T_Type file to Java file . . .");
                    fis = new FileInputStream(inputFile);
                    Translator translator = new Translator(fis, javaOut);
                    translator.getChar(fis);
                    do {
                        translator.lex();
                    } while (translator.nextToken != "-1" && translator.continueScanning == true && translator.lexLen < 100);
                    translator.out.close();
                }


            }



        }
    }
}
