import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyzer
{
    LexicalAnalyzer(FileInputStream inFis)
    {
        this.fis = inFis;
    }

    //Declaring Literals
    final int DECIMAL = -2;
    final int LETTER = 0;
    final int CONSTANT_DECLARATION = 3;
    final int DIGIT = 1;
    final int UNKNOWN = 99;
    final int FLOAT_LIT = 9;
    final int INT_LIT = 10;
    final int IDENT = 11;
    final int UNDERSCORE = 12;
    final int ASSIGN_OP = 20;
    final int ADD_OP = 21;
    final int SUB_OP = 22;
    final int MULT_OP = 23;
    final int DIV_OP = 24;
    final int LEFT_PAREN = 25;
    final int RIGHT_PAREN = 26;
    final int STRING_LITERAL = 27;

    final int SEMICOLON = 28;
    final int SPACE = 4;

    final int START_MAIN = 30;

    final int END_MAIN = 31;

    final int IF = 32;
    final int THEN = 33;
    final int END_IF = 34;

    final int ELSE = 35;
    final int END_ELSE = 36;

    final int FOR = 37;
    final int SEPERATOR = 38;
    final int LOOP = 39;
    final int END_FOR = 40;
    final int WHILE = 41;
    final int END_WHILE = 42;
    final int LINE_COMMENT = 43;
    final int MULTI_LINE_COMMENT_BEGIN = 44;
    final int MULTI_LINE_COMMENT_END = 45;

    final int EQUAL_RELATION = 50;
    final int OR_RELATION = 51;
    final int LESS_RELATION = 52;
    final int GREATER_RELATION = 53;

    final int LESS_EQUAL_RELATION = 54;
    final int GREATER_EQUAL_RELATION = 55;

    final int TRUE = 54;
    final int FALSE = 55;

    final int INTEGER_DECLARATION = 60;
    final int FLOAT_DECLARATION = 61;
    final int STRING_DECLARATION = 62;
    final int BOOLEAN_DECLARATION = 63;
    final int CHARACTER_DECLARATION = 64;

    final int LENGTH_FUNCTION = 100;
    final int TOUPPER_FUNCTION = 101;
    final int TOLOWER_FUNCTION = 102;
    final int CONSOLE_FUNCTION = 103;
    final int FUNCTION_DECLARATION = 104;
    final int COMMA_SEPARATOR = 105;
    final int RETURNS_FUNCTION_COMMAND = 106;
    final int VOID_IDENTIFIER_TYPE = 107;
    final int FUNCTION_END_STATEMENT = 108;




    //Global variables used for parsing and storing
    FileInputStream fis;
    int lineNumber = 1;
    int charClass;
    char lexeme[] = new char[100];
    ArrayList<Character> lexemeList = new ArrayList<Character>();

    int decimalCount = 0;

    char nextChar;
    int lexLen;
    int functionLexeme;
    int nextToken;
    int currentCount = 0;

    boolean endOfFile = false;
    boolean isValidFunction = false;
    boolean checkCurrentChar = false;
    boolean isValid = true;
    char bufferChar;
    String functionCommand = "";



    /*****************************************************/





/* lookup - a function to lookup operators and parentheses
and return the token */
    int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                checkRelation();
                if(lexLen == 1)
                    nextToken = MULT_OP;
                else {
                    for(char c : lexeme)
                        System.out.print(c);
                    if(lexLen == 2 && lexeme[lexLen-1] == '#')
                        nextToken = MULTI_LINE_COMMENT_END;
                }
                break;
            case '#':
                checkRelation();
                if(lexLen == 1)
                    nextToken = LINE_COMMENT;
                else {
                    if(lexLen == 2 && lexeme[lexLen-1] == '*')
                        nextToken = MULTI_LINE_COMMENT_BEGIN;
                }
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            case ',':
                addChar();
                nextToken = COMMA_SEPARATOR;
                break;
            case '=':
                checkRelation();
                //while (charClass == UNKNOWN) {
                 //   addChar();
                 //   getChar(fis);
               // }
                //System.out.println("Lex Len: " + lexLen);
                if(lexLen == 2) {
                    nextToken = EQUAL_RELATION;
                    break;
                }
                else {
                    nextToken = ASSIGN_OP;
                    break;
                }
            case '<':
                checkRelation();
                if(lexLen == 2) {
                    nextToken = LESS_EQUAL_RELATION;
                    break;
                }
                else
                {
                    nextToken = LESS_RELATION;
                    break;
                }
            case '>':
                checkRelation();
                if(lexLen == 2) {
                    nextToken = GREATER_EQUAL_RELATION;
                    break;
                }
                else {
                    nextToken = GREATER_RELATION;
                    break;
                }
            case '|':
                checkRelation();
                if(lexLen == 2) {
                    nextToken = OR_RELATION;
                    break;
                }
                else {
                    //System.out.println("Error on Line " + lineNumber + "!");
                    nextToken = -1;
                    isValid = false;
                    break;
                }
            case ';':
                addChar();
                nextToken = SEMICOLON;
                break;
            default:
                //addChar();
                //isValid = false;
                nextToken = -1;
                break;
        }
        return nextToken;
    }


    void checkRelation()
    {
        while (charClass == UNKNOWN && !Character.isWhitespace(nextChar)) {
            addChar();
            getChar(fis);
        }
        checkCurrentChar = true;
        bufferChar = nextChar;
        System.out.println(lexLen);
    }

    /*****************************************************/
/* addChar - a function to add nextChar to lexeme */
    void addChar()
    {
        if (lexLen <= 98)
        {
            lexemeList.add(nextChar);
            lexeme[lexLen++] = nextChar;
            //lexeme[lexLen] = 0;
        } else
            System.out.println("Error - lexeme is too long");
    }

    /*****************************************************/


    boolean checkCommand()
    {
        String command = "";

        for(char letter : lexemeList) {
            command += letter;
        }

//        for(Character letter : lexemeList)
//        {
//            System.out.print(letter);
//        }
//        System.out.println();


        command = command.trim();
        //System.out.println(command);
        boolean result = false;

        switch(command)
        {
            case "START_MAIN":
                nextToken = START_MAIN;
                result = true;
                break;
            case "END_MAIN":
                nextToken = END_MAIN;
                result = true;
                break;
            case "CONSTANT":
                nextToken = CONSTANT_DECLARATION;
                result = true;
                break;
            case "integer":
                nextToken = INTEGER_DECLARATION;
                result = true;
                break;
            case "string":
                nextToken = STRING_DECLARATION;
                result = true;
                break;
            case "float":
                nextToken = FLOAT_DECLARATION;
                result = true;
                break;
            case "boolean":
                nextToken = BOOLEAN_DECLARATION;
                result = true;
                break;
            case "character":
                nextToken = CHARACTER_DECLARATION;
                result = true;
                break;
            case "console":
                nextToken = CONSOLE_FUNCTION;
                result = true;
                break;
            case "IF":
                nextToken = IF;
                result = true;
                break;
            case "END_IF":
                nextToken = END_IF;
                result = true;
                break;
            case "THEN":
                nextToken = THEN;
                result = true;
                break;
            case "WHILE":
                nextToken = WHILE;
                result = true;
                break;
            case "FUNCTION":
                nextToken = FUNCTION_DECLARATION;
                result = true;
                break;
            case "RETURNS":
                nextToken = RETURNS_FUNCTION_COMMAND;
                result = true;
                break;
            case "RETURN":
                nextToken = RETURNS_FUNCTION_COMMAND;
                result = true;
                break;
            case "true":
                nextToken = TRUE;
                result = true;
                break;
            case "false":
                nextToken = FALSE;
                result = true;
                break;
            case "void":
                nextToken = VOID_IDENTIFIER_TYPE;
                result = true;
                break;
            case "END_FUNCTION":
                nextToken = FUNCTION_END_STATEMENT;
                result = true;
                break;

        }

        return result;
    }


/* getChar - a function to get the next character of
input and determine its character class */
    void getChar(FileInputStream inputFile)
    {
        if(checkCurrentChar == true)
        {
            checkCurrentChar = false;
            nextChar = bufferChar;
        }
        else
        {
            try {
                nextChar = (char) inputFile.read();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        if(nextChar == '"')
            charClass = STRING_LITERAL;
        else
        {
            if(nextChar == '.')
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
 }



    /*****************************************************/
/* getNonBlank - a function to call getChar until it
returns a non-whitespace character */
    void getNonBlank()
    {
        while(Character.isSpaceChar(nextChar) || Character.isWhitespace(nextChar)) {
            getChar(fis);
            if(nextChar == '\n')
                lineNumber++;
        }
    }

    /*****************************************************/

    /* lex - a simple lexical analyzer for arithmetic
    expressions */
    int lex()
    {
        lexLen = 0;
        getNonBlank();
        switch (charClass)
        {
            /* Parse identifiers */
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
                    nextToken = IDENT;

                if(charClass == DECIMAL && decimalCount == 1)
                {
                    getChar(fis);

                    while (charClass == LETTER) {
                        functionCommand += nextChar;
                        getChar(fis);
                    }


                    functionOperation(functionCommand);

                    if(isValidFunction)
                        System.out.print("Function Token = " + functionLexeme + ", Function Lexeme " + functionCommand + " on ");
                    else {
                        System.out.println("[!] Error. Invalid Function Name: " + functionCommand);
                        isValid = false;
                    }
                }
                else if (decimalCount > 1)
                    isValid = false;

                break;
            /* Parse integer literals */
            case DIGIT:
                addChar();
                getChar(fis);
                while (charClass == DIGIT || charClass == DECIMAL)
                {
                    addChar();
                    getChar(fis);
                    //checkCurrentChar = true;
                }
                //bufferChar = nextChar;
                if(decimalCount == 1)
                    nextToken = FLOAT_LIT;
                else if(decimalCount == 0)
                    nextToken = INT_LIT;
                else
                    isValid = false;
                break;
            /* Parentheses and operators */
            case UNKNOWN:
                lookup(nextChar);
                getChar(fis);
                if(nextToken == -1)
                    endOfFile = true;
                break;
            /* null */
            case -1:
                nextToken = -1;
                lexemeList.add('E');
                lexemeList.add('O');
                lexemeList.add('F');
                lexemeList.add('0');
                break;
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

                nextToken = STRING_LITERAL;
                break;
            default:
                System.out.println("[!] Error at character '" + nextChar + "' on line: " + lineNumber);
                nextToken = -1;
                isValid = false;
        } /* End of switch */

        printResult();
        clearLexemeArray();


        return nextToken;
    } /* End of function lex */

    void functionOperation(String function)
    {
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
    }

    void clearLexemeArray()
    {
        Arrays.fill(lexeme, ' ');
        lexemeList.clear();
        decimalCount = 0;
    }

    void printResult()
    {
        if(endOfFile == false)
        {
            String strLexeme = "";
            for (Character a : lexemeList) {
                strLexeme += a;
            }

            if(isValid)
                System.out.println("Token: " + nextToken + ", Lexeme: " + strLexeme);
            else
            {
                isValid = true;
                System.out.println("[!] Error -- Invalid Lexeme '" + strLexeme + "' on line " + lineNumber);
            }
        }
        else
        {
            System.out.println("<end of file>");
        }



    }
}
