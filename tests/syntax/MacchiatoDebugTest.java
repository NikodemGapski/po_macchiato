package syntax;

import expression.*;
import syntax.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import syntax.exceptions.InvalidVariableNameException;

import java.io.ByteArrayInputStream;

public class MacchiatoDebugTest {
    private static Macchiato forVariableReassign;
    private static Macchiato allInstructions;
    @BeforeAll
    static void prepare() throws NullArgumentException, InvalidVariableNameException {
        forVariableReassign = new Macchiato(new Block(
                new Declaration[]{},
                new Instruction[]{
                        new ForLoop(
                                'i', new Constant(5),
                                new Instruction[]{
                                        new Print(new Variable('i')),
                                        new Assignment('i', new Constant(-1))
                                }
                        )
                }
        ));
        allInstructions = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('a', new Constant(2)),
                        new Declaration('b', new Constant(4))
                },
                new Instruction[]{
                        new IfStatement(
                                new Variable('a'), IfStatement.Type.Less, new Variable('b'), new Instruction[]{
                                        new IfStatement(new Variable('a'), IfStatement.Type.Equal, new Variable('b'), new Instruction[]{}, new Instruction[]{
                                                new Block(
                                                        new Declaration[]{
                                                                new Declaration('x', new Constant(1))
                                                        },
                                                        new Instruction[]{
                                                                new Print(new Subtraction(new Variable('b'), new Variable('a'))),
                                                                new ForLoop('i', new Subtraction(new Variable('b'), new Variable('a')), new Instruction[]{
                                                                        new Assignment('x', new Addition(new Variable('x'), new Variable('i')))
                                                                })
                                                        }
                                                )
                                        })
                        })
                }
        ));
    }
    @Test
    void stepExample() {
        setInput("s 1\n".repeat(20));
        forVariableReassign.debug();
    }
    @Test
    void stepExample2() {
        setInput("s 1\n".repeat(19));
        allInstructions.debug();
    }
    @Test
    void exceptionExample() throws Exception {
        setInput("s 1\n".repeat(7));
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('n', new Constant(0)),
                        new Declaration('m', new Constant(102))
                },
                new Instruction[]{
                        new Print(new Division(new Variable('m'), new Variable('n')))
                }
        ));
        m.debug();
    }
    @Test
    void displayExample() {
        setInput("s  3\n d 2\n d 1\n d 0\n c \n c \n c \n d 0\n d   5\nd 1\n");
        forVariableReassign.debug();
    }
    @Test
    void invalidCommand() {
        setInput("p 1 1r  f23  eqf q f23 \n          \n\n\nu\n\n\ne");
        forVariableReassign.debug();
    }

    private void setInput(String input) {
        input += "\ne";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}
