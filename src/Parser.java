import java.io.FileOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Parser {
    Scanner in;
    int currToken = -2;
    int prevToken = -3;
    int currDeclaration = -1;
    boolean endOfFile = false;
    boolean breakStatementBlock = false;
    boolean isValid = true;
    int lineCount = 1;

    Deque<Integer> stack = new ArrayDeque<Integer>();


    Parser(Scanner in/*, FileOutputStream outFile*/)
    {
        this.in = in;
        //this.out = outFile;
    }

    /**************** KEYWORD DECLARATIONS **************************/
    public final int NEW_LINE = -2;
    public final int DECIMAL = 5;
    final int LETTER = 0;
    final int CONSTANT_DECLARATION = 3;
    final int DIGIT = 1;
    final int UNKNOWN = 99;
    final int FLOAT_LIT = 9;
    final int INT_LIT = 10;
    final int IDENTIFIER_VARIABLE = 11;
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
    final int START_MAIN = 30;
    final int END_MAIN = 31;
    final int IF = 32;
    final int THEN = 33;
    final int END_IF = 34;
    final int ELSE_ID = 35;
    final int END_ELSE_ID = 36;
    final int FOR_LOOP_ID = 37;
    final int SEPERATOR = 38;
    final int LOOP_KEYWORD = 39;
    final int END_FOR_LOOP_ID = 40;
    final int WHILE_ID = 41;
    final int END_WHILE_ID = 42;
    final int LINE_COMMENT = 43;
    final int MULTI_LINE_COMMENT_BEGIN = 44;
    final int MULTI_LINE_COMMENT_END = 45;
    final int EQUAL_RELATION = 50;
    final int OR_RELATION = 51;
    final int LESS_RELATION = 52;
    final int GREATER_RELATION = 53;
    final int LESS_EQUAL_RELATION = 54;
    final int GREATER_EQUAL_RELATION = 55;
    final int TRUE_BOOLEAN = 56;
    final int FALSE_BOOLEAN = 57;
    final int AND_RELATION = 58;
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
    final int KEY_INPUT_READ = 109;
    final int INCREMENT_OPERATOR = 110;
    final int DECREMENT_OPERATOR = 111;
    /**************** END KEYWORD DECLARATIONS **********************/



//        void parseStatement(int in)
//        {
//
//            //checkTopLevel();
//
//            isLoopControlStart(in);
//
//
//
//        }

        int getNextToken()
        {

            if(in.hasNextInt())
            {

                currToken = in.nextInt();
//                System.out.println(currToken);
//                if(currToken == NEW_LINE)
//                {
//                    do
//                    {
//                        lineCount++;
//                        currToken = in.nextInt();
//                        System.out.println(currToken);
//                    }
//                    while (currToken != NEW_LINE);
//                }
//                System.out.println(currToken);
                return currToken;
            }
            else
            {
                endOfFile = true;
                return -1;
            }
        }

        void checkTopLevel(int in)
        {
            switch(in)
            {
                case MULTI_LINE_COMMENT_BEGIN:
                    do {
                        currToken = getNextToken();
                    } while(currToken != MULTI_LINE_COMMENT_END);
                    checkTopLevel(currToken);
                    break;
                case MULTI_LINE_COMMENT_END:
                    checkTopLevel(getNextToken());
                    break;
                case LINE_COMMENT:
                    break;
                default:
                    if(in == START_MAIN && stack.isEmpty())
                    {
                        lineCount++;
                        stack.add(in);
                        checkStatementBlock(getNextToken());
                    }
                    else if (in == END_MAIN && (stack.peek() == START_MAIN))
                    {
                        stack.pop();
                        stack.push(in);
                        checkTopLevel(getNextToken());
                    }
                    else if(in == FUNCTION_DECLARATION && (stack.peek() == END_MAIN))
                    {

                        currToken = getNextToken();
                        if(currToken == IDENTIFIER_VARIABLE)
                        {
                            currToken = getNextToken();
                            if(currToken == LEFT_PAREN)
                            {

                                while(currToken != RIGHT_PAREN && isValid == true)
                                {

                                    currToken = getNextToken();
                                    if (isValidDeclarationType(currToken)) {
                                        currToken = getNextToken();
                                        if (currToken == IDENTIFIER_VARIABLE)
                                        {
                                            currToken = getNextToken();
                                            if (currToken == COMMA_SEPARATOR)
                                            {
                                                isValid = true;
                                            }
                                            else if (currToken == RIGHT_PAREN)
                                                isValid = true;
                                            else
                                                isValid = false;
                                        }
                                        else
                                            isValid = false;
                                    } else
                                        isValid = false;
                                }

                                if(isValid == true)
                                {
                                    currToken = getNextToken();
                                    if (currToken == RETURNS_FUNCTION_COMMAND)
                                    {
                                        currToken = getNextToken();
                                        switch(currToken)
                                        {
                                            case VOID_IDENTIFIER_TYPE:
                                            case INTEGER_DECLARATION:
                                            case STRING_DECLARATION:
                                            case BOOLEAN_DECLARATION:
                                            case CHARACTER_DECLARATION:
                                            case FLOAT_DECLARATION:
                                                checkStatementBlock(getNextToken());
                                                break;
                                            default:
                                                isValid = false;
                                                printError();
                                        }
                                    }
                                    else
                                        printError();
                                }
                                else
                                {
                                    isValid = false;
                                    printError();
                                }
                            }
                        }

                    }
                    else if(in == -1)
                    {
                        if(stack.peek() == END_MAIN)
                            printResult();
                        else
                            printError();

                    }
                    else
                        isValid = false;

            }
        }

        boolean isValidDeclarationType(int in)
        {
            switch(in)
            {
                case VOID_IDENTIFIER_TYPE:
                case INTEGER_DECLARATION:
                case STRING_DECLARATION:
                case BOOLEAN_DECLARATION:
                case CHARACTER_DECLARATION:
                case FLOAT_DECLARATION:
                    return true;
                default:
                    return false;
            }
        }

        void checkStatementBlock(int in)
        {
            switch (in)
            {
                case MULTI_LINE_COMMENT_BEGIN:
                    do {
                       currToken = getNextToken();
                    } while(currToken != MULTI_LINE_COMMENT_END);
                    checkStatementBlock(currToken);
                    break;
                case MULTI_LINE_COMMENT_END:
                    checkStatementBlock(getNextToken());
                    break;
                case IDENTIFIER_VARIABLE:
                    currToken = getNextToken();
                    if(currToken == ASSIGN_OP)
                    {
                        checkAssignment(getNextToken());
                    }
                    else {
                        isValid = false;
                        printError();
                    }
                    break;
                case INTEGER_DECLARATION:
                    currDeclaration = INTEGER_DECLARATION;
                    checkDeclaration(getNextToken());
                    break;
                case STRING_DECLARATION:
                    currDeclaration = STRING_DECLARATION;
                    checkDeclaration(getNextToken());
                    break;
                case BOOLEAN_DECLARATION:
                    currDeclaration = BOOLEAN_DECLARATION;
                    checkDeclaration(getNextToken());
                    break;
                case CHARACTER_DECLARATION:
                    currDeclaration = CHARACTER_DECLARATION;
                    checkDeclaration(getNextToken());
                    break;
                case CONSTANT_DECLARATION:
                    checkConstant(getNextToken());
                    break;
                case FLOAT_DECLARATION:
                    currDeclaration = FLOAT_DECLARATION;
                    checkDeclaration(getNextToken());
                    break;
                case CONSOLE_FUNCTION:
                    checkConsoleFunction(getNextToken());
                    break;
                case WHILE_ID:
                    if(checkCondition(getNextToken()))
                    {
                        loopBodyExecution(END_WHILE_ID);
                    }
                    else
                    {
                        isValid = false;
                        printError();
                    }
                    break;
                case FOR_LOOP_ID:
                    checkForLoop();
                    break;
                case IF:
                    checkIfConditional();
                    break;
                default:
                    if(currToken == END_IF || currToken == END_WHILE_ID || currToken == END_FOR_LOOP_ID)
                    {
                        breakStatementBlock = true;
                    }
                    else if (currToken == FUNCTION_END_STATEMENT)
                    {
                        currToken = getNextToken();
                        if (currToken == IDENTIFIER_VARIABLE)
                            checkTopLevel(getNextToken());
                        else
                            printError();
                    }
                    else
                    {
                        isValid = false;
                        System.out.println("[!] Syntax Error near: " + currToken + " on line " + lineCount);

                    }
            }
        }


        void checkForLoop()
        {

            if (checkCondition(getNextToken()))
            {
                for (int i = 0; i < 2; i++)
                {
                    currToken = getNextToken();
                    if (currToken == SEPERATOR)
                    {
                        isValid = checkCondition(getNextToken());
                    }
                    else
                    {
                        isValid = false;
                        break;
                    }
                }
            }
            else
                isValid = false;



            if(isValid == true)
            {
                loopBodyExecution(END_FOR_LOOP_ID);
            }
            else
            {
                isValid = false;
                printError();
            }
        }

        void loopBodyExecution(int loopTypeEndID)
        {
            currToken = getNextToken();
            if(currToken == LOOP_KEYWORD)
            {
                currToken = getNextToken();
                while(currToken != loopTypeEndID && (breakStatementBlock == false) && isValid == true)
                {
                    checkStatementBlock(currToken);
                    //currToken = getNextToken();
                }
                checkNewLine();

            }
            else
            {
                isValid = false;
                printError();
            }
        }

        void checkIfConditional()
        {
            if(checkCondition(getNextToken()))
            {
                currToken = getNextToken();
                if(currToken == THEN)
                {
                    //STATEMENT BLOCK
                    currToken = getNextToken();
                    while((currToken != END_IF || currToken != ELSE_ID) && (breakStatementBlock == false) && isValid == true)
                    {
                        checkStatementBlock(currToken);
                        //currToken = getNextToken();
                    }
                    checkNewLine();
                }

            }
            else
            {
                printError();
                isValid = false;
            }
        }

        boolean checkCondition(int in)
        {
            switch(in)
            {
                case IDENTIFIER_VARIABLE:
                case INT_LIT:
                case FLOAT_LIT:
                    if(checkComparator(getNextToken()))
                    {
                        if(currToken != INCREMENT_OPERATOR && currToken != DECREMENT_OPERATOR)
                            currToken = getNextToken();
                        return true;
                    }
                    else
                    {
                        isValid = false;
                        printError();
                        return false;
                    }
                case TRUE_BOOLEAN:
                case FALSE_BOOLEAN:
                    return true;
                default:
                    printError();
                    return false;
            }

        }

        boolean checkComparator(int in)
        {
            switch(in)
            {
                case LESS_EQUAL_RELATION:
                case LESS_RELATION:
                case GREATER_EQUAL_RELATION:
                case GREATER_RELATION:
                case EQUAL_RELATION:
                case AND_RELATION:
                case OR_RELATION:
                case TRUE_BOOLEAN:
                case FALSE_BOOLEAN:
                case ASSIGN_OP:
                case INCREMENT_OPERATOR:
                case DECREMENT_OPERATOR:
                    return true;
                default:
                    return false;
            }
        }

        void checkConsoleFunction(int in)
        {
            if(in == LEFT_PAREN)
            {
                currToken = getNextToken();
                boolean validSyntax = true;
                while(validSyntax == true && currToken != RIGHT_PAREN)
                {
                    if (currToken == IDENTIFIER_VARIABLE || currToken == STRING_LITERAL)
                    {
                        currToken = getNextToken();
                        if (currToken == ADD_OP)
                        {
                            currToken = getNextToken();
                        }
                        else if(currToken != RIGHT_PAREN)
                            validSyntax = false;
                        else if(currToken == STRING_LITERAL)
                            currToken = getNextToken();
                    }
                    else
                        validSyntax = false;
                }

                if(currToken == RIGHT_PAREN && validSyntax == true)
                {
                    currToken = getNextToken();
                    if(currToken == SEMICOLON)
                        checkNewLine();
                    else
                        printError();
                }
                else
                    printError();

            }
            else
            {
                isValid = false;
                printError();
            }
        }

        void checkConstant(int in)
        {
            switch(in)
            {
                case FLOAT_DECLARATION:
                case INTEGER_DECLARATION:
                case STRING_DECLARATION:
                case BOOLEAN_DECLARATION:
                case CHARACTER_DECLARATION:
                    checkDeclaration(getNextToken());
                    break;
                default:
                    isValid = false;
                    printError();
            }
        }
        void checkDeclaration(int in)
        {

            if(in == IDENTIFIER_VARIABLE)
            {
                currToken = getNextToken();
                switch (currToken)
                {
                    case SEMICOLON:
                        checkNewLine();
                        break;
                    case ASSIGN_OP:
                        checkAssignment(getNextToken());
                        break;
                    default:
                        isValid = false;
                        printError();

                }

            }
            else {
                isValid = false;
                printError();

            }
        }


        void checkAssignment(int in)
        {
            switch (in)
            {
                case LEFT_PAREN:
                    stack.push(in);
                    checkAssignment(getNextToken());
                    break;
                case SUB_OP:
                    checkAssignment(getNextToken());
                    break;
                case INT_LIT:
                    verifyRightSide(INTEGER_DECLARATION);
                    break;
                case FLOAT_LIT:
                    verifyRightSide(FLOAT_DECLARATION);
                    break;
                case STRING_LITERAL:
                    verifyRightSide(STRING_DECLARATION);
                    break;
                case TRUE_BOOLEAN:
                case FALSE_BOOLEAN:
                    break;
                case IDENTIFIER_VARIABLE:
                    verifyRightSide(IDENTIFIER_VARIABLE);
                    break;
                default:
                    isValid = false;
                    printError();
            }
        }

        void checkOperator(int in)
        {
            switch(in)
            {
                case ADD_OP:
                case SUB_OP:
                case MULT_OP:
                case DIV_OP:
                    checkAssignment(getNextToken());
                    break;
                default:
                    isValid = false;
                    printError();
                    break;
            }
        }

        void verifyRightSide(int inType)
        {
            if(currDeclaration == inType || inType == IDENTIFIER_VARIABLE)
            {
                currToken = getNextToken();
                if (currToken == SEMICOLON)
                {
                    checkNewLine();
                    currDeclaration = -1;
                }
                else
                    checkOperator(currToken);
            }
            else {
                isValid = false;

                printError();
            }
        }

        void printError()
        {
            System.out.println("[!] Syntax Error near: " + currToken + " on line " + lineCount);
        }

        void checkNewLine()
        {
            if(isValid == true)
            {
                lineCount++;
                //Making sure to reset boolean to break away from statement block in case of conditional
                breakStatementBlock = false;
                currToken = getNextToken();

                if (currToken == END_MAIN) {
                    checkTopLevel(currToken);
                } else
                    checkStatementBlock(currToken);
            }
            else
                printError();
        }


//        void checkCommands(int in)
//        {
//
//            System.out.println(in);
//            switch(in)
//            {
//                case ASSIGN_OP:
//                    checkCommands(getNextToken());
//                case IDENTIFIER_VARIABLE:
//                    checkCommands(getNextToken());
//                    break;
//                case INTEGER_DECLARATION:
//                    checkCommands(getNextToken());
//                    break;
//                case STRING_DECLARATION:
//                    break;
//                case SEMICOLON:
//                    System.out.println("end of line");
//                    lineCount++;
//                    checkCommands(getNextToken());
//                    break;
//                case END_MAIN:
//                    currToken = getNextToken();
//                    if(currToken == -1)
//                    {
//                        endOfFile = true;
//                        printResult();
//                    }
//                    else
//                        checkCommands(currToken);
//                    break;
//                default:
//                    isValid = false;
//                    System.out.println("[!] Syntax Error near: " + currToken + " on line " + lineCount);
//
//            }
//        }

        void printResult()
        {
            if(isValid == true)
                System.out.println("[~] Parser Check Passed\n");
            else
                System.out.println("[!] Error: Parser Check Failed\n");

        }

//        boolean isLoopControlStart(int inCommand)
//        {
//            if (inCommand == WHILE_ID || inCommand == FOR_LOOP_ID)
//            {
//                return true;
//            }
//            else
//                return false;
//        }
}
