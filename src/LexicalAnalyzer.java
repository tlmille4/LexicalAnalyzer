import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class LexicalAnalyzer
{
    LexicalAnalyzer(FileInputStream inFis)
    {
        this.fis = inFis;
    }

    //Declaring Literals
    final int LETTER = 0;
    final int DIGIT = 1;
    final int UNKNOWN = 99;
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
    final int MULTI_LINE_COMMENT = 44;

    final int EQUAL_RELATION = 50;
    final int OR_RELATION = 51;
    final int LESS_RELATION = 52;
    final int GREATER_RELATION = 53;




    //Global variables used for parsing and storing
    FileInputStream fis;
    int charClass;
    char lexeme[] = new char[100];
    char nextChar;
    int lexLen;
    int token;
    int nextToken;
    int currentCount = 0;

    String[] commandArray;

    String line = "";
    String currentCommand = "";

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
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            case '=':
                while (charClass == UNKNOWN) {
                    addChar();
                    getChar(fis);
                }
                System.out.println("Lex Len: " + lexLen);
                if(lexLen == 2) {
                    nextToken = EQUAL_RELATION;
                    break;
                }
                else {
                    nextToken = ASSIGN_OP;
                    break;
                }
            case '<':
                addChar();
                nextToken = LESS_RELATION;
                break;
            case '>':
                addChar();
                nextToken = GREATER_RELATION;
                break;
            case '|':
                checkRelation();
                if(lexLen == 2) {
                    nextToken = OR_RELATION;
                    break;
                }
                else {
                    nextToken = -1;
                    break;
                }
            default:
                addChar();
                nextToken = -1;
                break;
        }
        return nextToken;
    }


    void checkRelation()
    {
        while (charClass == UNKNOWN) {
            addChar();
            getChar(fis);
        }
        System.out.println(lexLen);
    }

    /*****************************************************/
/* addChar - a function to add nextChar to lexeme */
    void addChar()
    {
        if (lexLen <= 98)
        {
            lexeme[lexLen++] = nextChar;
            //lexeme[lexLen] = 0;
        } else
            System.out.println("Error - lexeme is too long");
    }

    /*****************************************************/


    boolean checkCommand()
    {
        String command = "";

        for(char letter : lexeme) {
            command += letter;
        }

        command = command.trim();
        System.out.println(command);
        boolean result = false;

        switch(command)
        {
            case "START_MAIN":
                nextToken = START_MAIN;
                result = true;
                break;
            case "END_MAIN":
                nextToken = END_MAIN;
                break;
            case "integer":
                break;
            case "string":
                break;
            case "float":
                break;
            case "boolean":
                break;
            case "character":
                break;
            case "console":
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

        }

        return result;
    }


/* getChar - a function to get the next character of
input and determine its character class */
    void getChar(FileInputStream inputFile)
    {

        //HAVE FUNCTION GETCOMMAND IF NOT, THEN DO GET CHAR
        //char currentChar = inCharArr[currentCount];
        try {
            nextChar = (char) inputFile.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Character.isSpaceChar(nextChar))
            charClass = SPACE;
        else
        {
            if (Character.isDefined(nextChar))
            {
                //System.out.println(nextChar);
                if (Character.isAlphabetic(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else if (nextChar == '_') charClass = UNDERSCORE;
                else charClass = UNKNOWN;
            }
            else
            {
                charClass = -1;
            }
        }
 }



    /*****************************************************/
/* getNonBlank - a function to call getChar until it
returns a non-whitespace character */
    void getNonBlank()
    {
        while(Character.isSpaceChar(nextChar))
            getChar(fis);
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
                addChar();
                getChar(fis);
                while (charClass == LETTER || charClass == DIGIT || charClass == UNDERSCORE) {
                    addChar();
                    getChar(fis);
                }
                if(!checkCommand())
                    nextToken = IDENT;
                //else
                    //nextToken = START_MAIN;
                break;
            /* Parse integer literals */
            case DIGIT:
                addChar();
                getChar(fis);
                while (charClass == DIGIT)
                {
                    addChar();
                    getChar(fis);
                }
                nextToken = INT_LIT;
                break;
            /* Parentheses and operators */
            case UNKNOWN:
                lookup(nextChar);
                getChar(fis);
                break;
            /* null */
            case -1:
                nextToken = -1;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                break;
        } /* End of switch */


        System.out.print("Next token is: "+ nextToken +", Next lexeme is ");
        for(char a : lexeme) {
            System.out.print(a);
        }
        System.out.println();
        Arrays.fill(lexeme, ' ');
        return nextToken;
    } /* End of function lex */
}
