import javax.naming.ldap.Control;
import java.util.ArrayDeque;
import java.util.Deque;

public class Parser {

    boolean isValid = true;
    enum ControlKeyword {WHILE, LOOP, FOR_LOOP_ID};

    Deque<Integer> stack = new ArrayDeque<Integer>();

    /**************** KEYWORD DECLARATIONS **************************/
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



        void parseStatement(int in)
        {
            checkStart(in);

            isLoopControlStart(in);



        }

        void checkStart(int in)
        {
            if(in == START_MAIN)
            {
                if (stack.isEmpty())
                    stack.add(in);
                else
                    isValid = false;
            }
        }

        boolean isLoopControlStart(int inCommand)
        {
            if (inCommand == WHILE_ID || inCommand == FOR_LOOP_ID)
            {
                return true;
            }
            else
                return false;
        }
}