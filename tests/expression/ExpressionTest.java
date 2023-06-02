package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ModuloZeroException;
import org.junit.jupiter.api.Test;
import syntax.exceptions.UndefinedVariableException;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    @Test
    void evaluateAndPrint() throws Exception {
        // ((((-1) + 2) * 4 % 3) + 24 / 2) / 3 = 4
        Expression e = new Division(
                new Addition(
                        new Modulo(
                                new Multiplication(
                                        new Addition(
                                                new Constant(-1),
                                                new Constant(2)
                                        ),
                                        new Constant(4)
                                ),
                                new Constant(3)
                        ),
                        new Division(
                                new Constant(24),
                                new Constant(2)
                        )
                ),
                new Constant(3)
        );
        assertEquals(4, e.evaluate(null));
        assertEquals("((((-1) + 2) * 4 % 3) + 24 / 2) / 3", e.toString());
    }
    @Test
    void divisionByZero() throws Exception {
        Expression e = new Division(new Constant(20), new Addition(new Constant(-3), new Constant(3)));
        assertThrows(DivisionByZeroException.class, () -> e.evaluate(null));
        try {
            e.evaluate(null);
        }catch(DivisionByZeroException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    void modulo() throws Exception {
        Expression e2 = new Addition(new Constant(1), new Modulo(new Constant(4), new Constant(0)));
        Expression e3 = new Addition(new Constant(1), new Modulo(new Constant(4), new Constant(2)));
        assertThrows(ModuloZeroException.class, () -> e2.evaluate(null));
        assertEquals(1, e3.evaluate(null));

        try {
            e2.evaluate(null);
        }catch(ModuloZeroException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    void noArgument() throws Exception {
        Expression e = new Subtraction(new Constant(20), new Variable('a'));
        assertThrows(UndefinedVariableException.class, () -> e.evaluate(null));
        try {
            e.evaluate(null);
        }catch(UndefinedVariableException ex) {
            System.out.println(ex.getMessage());
        }
    }
}