import java.io.*;
import java.util.ArrayList;

/**
 * Class: Translator
 * This class is used to translate the T_Type input file into a Java program
 */
public class Translator
{
    /**************** KEYWORD DECLARATIONS **************************/
    public final int DECIMAL = 5;
    final int LETTER = 0;
    final int DIGIT = 1;
    final int UNKNOWN = 99;
    final int UNDERSCORE = 12;
    final int STRING_LITERAL = 27;
    final int LENGTH_FUNCTION = 100;
    final int TOUPPER_FUNCTION = 101;
    final int TOLOWER_FUNCTION = 102;
    /**************** END KEYWORD DECLARATIONS **********************/
    //Global variables used for parsing and storing
    FileInputStream fis;
    BufferedWriter out;

    int lineNumber = 1;
    int charClass;
    ArrayList<Character> lexemeList = new ArrayList<Character>();
    int decimalCount = 0;
    char nextChar;
    int lexLen;
    int functionLexeme;
    String nextToken;
    boolean endOfFile = false;
    boolean checkCurrentChar = false;
    boolean isValid = true;
    boolean continueScanning = true;
    char bufferChar;
    String functionCommand = "";

    /**
     * LexicalAnalyzer(FileInputStream)
     * Parameter Constructor that requires user to pass File Input Stream to class
     * in order to get input file for Lexical Analysis.
     * @param inFis
     */
    Translator(FileInputStream inFis, File inFile)
    {
        this.fis = inFis;

        try {

            this.out = new BufferedWriter(new FileWriter(inFile));
        } catch(IOException ex)
        {
            System.err.println(ex);
        }
    }

    /**
     * lookup()
     * This function is used to lookup unknown operators found in the input file.
     * It will return the token number if found, else it will return -1
     * @param ch - the unknown character to be checked
     * @return nextToken
     */
        String lookup(char ch)
        {
            switch (ch)
            {
                case '(':
                    addChar();
                    nextToken = "(";
                    break;
                case ')':
                    addChar();
                    nextToken = ")";
                    break;
                case '+':
                    checkRelation();
                    if(lexLen == 2 && getLexemeArrayContents().equals("++")) {
                        nextToken = "++ ";
                        break;
                    }
                    else if(lexLen == 1)
                    {
                        nextToken = "+ ";
                        break;
                    }
                    else
                        isValid = false;
                    break;
                case '-':
                    checkRelation();
                    if(lexLen == 2 && getLexemeArrayContents().equals("--")) {
                        nextToken = "-- ";
                        break;
                    }
                    else if(lexLen == 1)
                    {
                        nextToken = "- ";
                        break;
                    }
                    else
                        isValid = false;
                    break;
                case '*':
                    checkRelation();
                    if(lexLen == 1)
                        nextToken = "* ";
                    else {
                        if(lexLen == 2 && getLexemeArrayContents().equals("*#"))
                            nextToken = "*/\n";
                    }
                    break;
                case '#':
                    checkRelation();
                    if(lexLen == 1) {
                        nextToken = "//";
                    }
                    else {
                        if(lexLen == 2 && getLexemeArrayContents().equals("#*"))
                            nextToken = "/*";
                    }
                    break;
                case '/':
                    addChar();
                    nextToken = "/ ";
                    break;
                case ',':
                    addChar();
                    nextToken = ", ";
                    break;
                case '=':
                    checkRelation();
                    if(lexLen == 2) {
                        nextToken = "== ";
                        break;
                    }
                    else {
                        nextToken = "= ";
                        break;
                    }
                case '<':
                    checkRelation();
                    if(lexLen == 2 && getLexemeArrayContents().equals("<=")) {
                        nextToken = "<= ";
                        break;
                    }
                    else if(lexLen == 1)
                    {
                        nextToken = "< ";
                        break;
                    }
                    else
                        isValid = false;
                    break;
                case '>':
                    checkRelation();
                    if(lexLen == 2 && getLexemeArrayContents().equals(">=")) {
                        nextToken = ">= ";
                        break;
                    }
                    else if(lexLen == 1) {
                        nextToken = "> ";
                        break;
                    }
                    else
                        isValid = false;
                    break;
                case '|':
                    checkRelation();
                    if(lexLen == 2) {
                        nextToken = "|| ";
                        break;
                    }
                    else {
                        if (lexLen == 1)
                            nextToken = "| ";
                        break;
                    }
                case ';':
                    addChar();
                    nextToken = ";\n";
                    break;
                default:
                    if(Character.isDefined(ch))
                        isValid = false;
                    else
                        nextToken = "-1";
                    break;
            }
            return nextToken;
        }

    /**
     * getLexemeArrayContents()
     * This function returns the lexeme as a string from lexemeArray
     * @return temp - String of characters in lexeme
     */
    String getLexemeArrayContents()
    {
        String temp = "";
        for(char a : lexemeList)
            temp += a;
        return temp;
    }

    /**
     * checkRelation()
     * This function is called to see whether or not an Unknown character (operator) is overloaded
     * such as the == >= <= operators
     */
    void checkRelation()
    {
        while (charClass == UNKNOWN && !Character.isWhitespace(nextChar) && Character.isDefined(nextChar) && nextChar != ';')
        {
            addChar();
            getChar(fis);
        }
        checkCurrentChar = true;
        bufferChar = nextChar;
    }

    /**
     * addChar()
     * This function adds a character to the lexeme array to create a word/string/character
     */
    void addChar()
    {
        if (lexLen <= 98)
        {
            lexemeList.add(nextChar);
            lexLen++;
        } else
            System.out.println("Error - lexeme is too long");
    }

    /**
     * checkCommand()
     * Checks to see if scanned in string is a keyword in T_Type
     * @return boolean -- true if a valid word is found
     */
    boolean checkCommand()
    {
        //Declaring local variables
        String command = "";
        boolean result = false;

        //Creating word from char array
        for(char letter : lexemeList)
            command += letter;
        command = command.trim();

        //Checking to see if word is a keyword in T_Type
        switch(command)
        {
            case "START_MAIN":
                nextToken = "public class Test {\n" +
                        "public static void main(String[] args) {\n";
                result = true;
                break;
            case "END_MAIN":
                nextToken = "}\n}";
                result = true;
                break;
            case "CONSTANT":
                nextToken = "final ";
                result = true;
                break;
            case "integer":
                nextToken = "int ";
                result = true;
                break;
            case "string":
                nextToken = "String ";
                result = true;
                break;
            case "float":
                nextToken = "float ";
                result = true;
                break;
            case "boolean":
                nextToken = "boolean ";
                result = true;
                break;
            case "character":
                nextToken = "char ";
                result = true;
                break;
            case "console":
                nextToken = "System.out.println";
                result = true;
                break;
            case "IF":
                nextToken = "if(";
                result = true;
                break;
            case "END_IF":
                nextToken = "}\n";
                result = true;
                break;
            case "THEN":
                nextToken = ")\n{\n";
                result = true;
                break;
            case "WHILE":
                nextToken = "while(";
                result = true;
                break;
            case "FOR":
                nextToken = "for(";
                result = true;
                break;
            case "END_FOR":
                nextToken = "\n}";
                result = true;
                break;
            case "LOOP":
                nextToken = ")\n{\n";
                result = true;
                break;
            case "END_WHILE":
                nextToken = "\n}";
                result = true;
                break;
            case "END_ELSE":
                nextToken = "\n}\n";
                result = true;
                break;
            case "ELSE":
                nextToken = "}\nelse\n{\n";
                result = true;
                break;
            case "true":
                nextToken = "true ";
                result = true;
                break;
            case "false":
                nextToken = "false ";
                result = true;
                break;
            case "void":
                nextToken = "void ";
                result = true;
                break;
            case "END_FUNCTION":
                nextToken = "\n}\n";
                result = true;
                break;
            case "key_input":
                nextToken = "";
                result = true;
                break;
            case "AND":
                nextToken = "&& ";
                result = true;
                break;
            case "OR":
                nextToken = "|| ";
                result = true;
                break;
        }
        return result;
    }

    /**
     * getChar()
     * This function is called to get a single character from the input file.
     * The class of that character is then determined and set
     * @param inputFile of FileInputStream - input file with T_Type Grammar
     */
    void getChar(FileInputStream inputFile)
    {
        //Check to see if there is an unchecked scanned character in buffer
        if(checkCurrentChar == true)
        {
            checkCurrentChar = false;
            nextChar = bufferChar;
        }
        else
        {
            //Read input character from file
            try {
                nextChar = (char) inputFile.read();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        //Determine the character class
        if(nextChar == '"')
            charClass = STRING_LITERAL;
        else if(nextChar == '.')
        {
            decimalCount++;
            charClass = DECIMAL;
        }
        else if(Character.isAlphabetic(nextChar))
            charClass = LETTER;
        else if (Character.isDigit(nextChar))
            charClass = DIGIT;
        else if (nextChar == '_')
            charClass = UNDERSCORE;
        else
            charClass = UNKNOWN;
    }

    /**
     * getNonBlank()
     * This function is used to skip over all white spaces and blank spaces until a new character is reached
     */
    void getNonBlank()
    {
        while(Character.isSpaceChar(nextChar) || Character.isWhitespace(nextChar))
        {
            getChar(fis);
            if(nextChar == '\n')
                lineNumber++;
        }
    }

    /**
     * functionOperation()
     * This function is called if a '.' is detected after an identifier declaration. This means that a string function
     * may have been called (ie, length, to upper, etc)
     * @param function - String that contains possible function command
     */
    boolean functionOperation(String function)
    {
        boolean isValidFunction = false;
        switch(function)
        {
            case "length":
                functionLexeme = LENGTH_FUNCTION;
                isValidFunction = true;
                break;
            case "toUpperCase":
                functionLexeme = TOUPPER_FUNCTION;
                isValidFunction = true;
                break;
            case "toLowerCase":
                functionLexeme = TOLOWER_FUNCTION;
                isValidFunction = true;
                break;
        }

        return isValidFunction;
    }

    /**
     * clearLexemeArray()
     * This function is called to clear the current lexeme array in order to start a new lex iteration
     */
    void clearLexemeArray()
    {
        lexemeList.clear();
        decimalCount = 0;
    }

    /**
     * printResult()
     * This function is called to print the lexeme/token results from the lex() function
     * In even of error, it will print error condition
     */
    void printResult()
    {
        if(endOfFile == false)
        {
            if(isValid)
            {
                try{
                    out.write(nextToken);
                } catch (IOException ex)
                {
                    System.err.println(ex);
                }

            }
            else
            {
                isValid = true;
                continueScanning = false;
                System.out.println("[!] Error -- Invalid Syntax on line " + lineNumber);
            }
        }
        else
        {
            System.out.println("[~] T_Type to Java file Conversion Complete.");
            try{
                out.close();
            }
            catch(IOException ex)
            {
                System.out.println(ex);
            }


        }
    }

    /**
     * lex()
     * This function implements most other functions in the Lexical Analyzer class. It will determine each token in a
     * given input file
     * @return returns the nextToken number. Returns -1 if end of file or error
     */
    String lex()
    {
        lexLen = 0;
        getNonBlank();
        switch (charClass)
        {
            //If class of character is letter, check to see if identifier or keyword
            case LETTER:
                //Adding character to lexeme array and getting next character
                addChar();
                getChar(fis);
                //Getting the rest of the word
                while (charClass == LETTER || charClass == DIGIT || charClass == UNDERSCORE) {
                    addChar();
                    getChar(fis);
                }
                //Checking if identifier with function appended
                if(!checkCommand())
                    nextToken = getLexemeArrayContents() + " ";

                //Checking to see if the identifier has a function attachment
                if(charClass == DECIMAL && decimalCount == 1)
                {
                    getChar(fis);

                    while (charClass == LETTER) {
                        functionCommand += nextChar;
                        getChar(fis);
                    }

                    if(functionOperation(functionCommand))
                        System.out.printf("%-10d%-30s%n", functionLexeme, functionCommand);
                    else {
                        System.out.println("[!] Error. Invalid Function Name: " + functionCommand);
                        isValid = false;
                    }
                }
                else if (decimalCount > 1)
                    isValid = false;
                break;

            //If character is a digit, check to see if its and integer/floating point number
            case DIGIT:
                addChar();
                getChar(fis);
                while (charClass == DIGIT || charClass == DECIMAL)
                {
                    addChar();
                    getChar(fis);
                }
                if(decimalCount == 1)
                    nextToken = getLexemeArrayContents();
                else if(decimalCount == 0)
                    nextToken = getLexemeArrayContents();
                else
                    isValid = false;
                break;

            //Check for unknown operators
            case UNKNOWN:
                lookup(nextChar);
                getChar(fis);
                if(nextToken == "-1")
                    endOfFile = true;
                break;

            //If " is found, get the rest of the string literal
            case STRING_LITERAL:
                do
                {
                    addChar();
                    getChar(fis);
                }
                while(charClass != STRING_LITERAL);
                //Getting last " from file
                addChar();
                getChar(fis);
                nextToken = getLexemeArrayContents();
                break;

            //Default print error in case of some unknown lex condition
            default:
                System.out.println("[!] Error on line: " + lineNumber);
                nextToken = "-1";
                endOfFile = true;
                isValid = false;
        }

        //Print result from case to screen and clear lexeme array for next token
        printResult();
        clearLexemeArray();

        return nextToken;
    }

}
