import javax.xml.stream.events.EndDocument;
import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        File inputFile;
        FileInputStream fis;

        inputFile = new File("src/in.txt");
        fis = new FileInputStream(inputFile);
        LexicalAnalyzer lex = new LexicalAnalyzer(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));


        String inLine;

        if (!(inputFile.exists()))
            System.out.println("Error opening input file");
        else
        {
            //inLine = null;
            //while((inLine = br.readLine()) != null)
            //{
                //lex.line = inLine;
                //lex.startValidation();
                lex.getChar(fis);
                do {
                    lex.lex();
                } while (lex.nextToken != -1);
            //}
        }
    }
}
