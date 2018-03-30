import java.io.*;

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
        LexicalAnalyzer lex = new LexicalAnalyzer(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        if (!(inputFile.exists()))
            System.out.println("Error opening input file");
        else
        {
            //Start lexical analyzer
            System.out.printf("%-10s%-30s%n", "Token", "Lexeme");
            System.out.printf("%-10s%-30s%n", "-----", "------");
            lex.getChar(fis);
            do {
                lex.lex();
            } while (lex.nextToken != -1 && lex.continueScanning == true);
        }
    }
}
