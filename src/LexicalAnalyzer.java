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
    final int TRUE = 54;
    final int FALSE = 55;

    final int INTEGER_DECLARATION = 60;
    final int FLOAT_DECLARATION = 61;
    final int STRING_DECLARATION = 62;
    final int BOOLEAN_DECLARATION = 63;
    final int CHARACTER_DECLARATION = 64;




    //Global variables used for parsing and storing
    FileInputStream fis;
    int charClass;
    char lexeme[] = new char[100];
    ArrayList<Character> lexemeList = new ArrayList<Character>();

    int decimalCount = 0;

    char nextChar;
    int lexLen;
    int token;
    int nextToken;
    int currentCount = 0;

    boolean checkCurrentChar = false;
    boolean isValid = true;
    char bufferChar;

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
                System.out.println(ch + " DONT EXIST");
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

        for(char letter : lexeme) {
            command += letter;
        }

        for(Character letter : lexemeList)
        {
            System.out.print(letter);
        }
        System.out.println();


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
            case "true":
                nextToken = TRUE;
                result = true;
                break;
            case "false":
                nextToken = FALSE;
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


        if(Character.isSpaceChar(nextChar)) {
            charClass = SPACE;
        }
        else
        {
            //if (Character.isDefined(nextChar))
            //{
                //System.out.println(nextChar);
            if(nextChar == '.') {
                decimalCount++;
                charClass = DECIMAL;
            }
            else if (Character.isAlphabetic(nextChar))
                    charClass = LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = DIGIT;
                else if (nextChar == '_') charClass = UNDERSCORE;
                else charClass = UNKNOWN;
            //}
            //else
            //{
             //   charClass = -1;
            //}
        }
 }



    /*****************************************************/
/* getNonBlank - a function to call getChar until it
returns a non-whitespace character */
    void getNonBlank()
    {
        while(Character.isSpaceChar(nextChar) || Character.isWhitespace(nextChar))
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
                break;
            /* null */
            case -1:
                nextToken = -1;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                lexemeList.add('E');
                lexemeList.add('O');
                lexemeList.add('F');
                lexemeList.add('0');

                break;
            case SPACE:
                System.out.println("Test");
                break;
        } /* End of switch */

        printResult();
        clearLexemeArray();


        return nextToken;
    } /* End of function lex */

    void clearLexemeArray()
    {
        Arrays.fill(lexeme, ' ');
        lexemeList.clear();
        decimalCount = 0;
    }

    void printResult()
    {
        String strLexeme = "";
        for (Character a : lexemeList) {
            strLexeme += a;
        }

        if(isValid)
        {
            System.out.println("Next token is: " + nextToken + ", Next lexeme is " + strLexeme);

//            for (char a : lexeme) {
//                System.out.print(a);
//            }
//
//            System.out.println();
//
//
//            for (Character a : lexemeList) {
//                System.out.print(a);
//            }
//            System.out.println();
        }
        else
        {
            isValid = true;
            System.out.println("[!] Error -- Invalid Lexeme: " + strLexeme);
        }


    }
}
