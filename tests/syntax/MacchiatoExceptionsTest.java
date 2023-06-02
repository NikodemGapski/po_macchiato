package syntax;

import expression.Constant;
import expression.Division;
import expression.Modulo;
import expression.Variable;
import org.junit.jupiter.api.Test;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.MacchiatoCompilationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MacchiatoExceptionsTest {
    @Test
    void exceptionRepeatedDeclaration() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('a', new Constant(1)),
                        new Declaration('b', new Constant(2)),
                        new Declaration('a', new Constant(1))
                },
                new Instruction[]{}
        ));
        m.execute();
    }
    @Test
    void exceptionUndefineVariable() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('x', new Constant(-1))
                },
                new Instruction[]{
                        new Assignment('x', new Constant(0)),
                        new Assignment('y', new Constant(10))
                }
        ));
        m.execute();
    }
    @Test
    void exceptionExpressionArithmetic() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('n', new Constant(0)),
                        new Declaration('m', new Constant(102))
                },
                new Instruction[]{
                        new Print(new Division(new Variable('m'), new Variable('n')))
                }
        ));
        m.execute();
    }
    @Test
    void exceptionExpressionArithmetic2() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('a', new Constant(1))
                },
                new Instruction[]{
                        new IfStatement(
                                new Constant(0),
                                IfStatement.Type.Less,
                                new Modulo(new Variable('a'), new Constant(0)),
                                new Instruction[]{}
                        )
                }
        ));
        m.execute();
    }
    @Test
    void exceptionInvalidName() {
        assertThrows(InvalidVariableNameException.class, () -> new ForLoop('0', new Constant(2), new Instruction[]{}));
        try {
            new ForLoop('0', new Constant(2), new Instruction[]{});
        }catch(MacchiatoCompilationException e) {
            System.out.println(e.getMessage());
        }
    }
}
