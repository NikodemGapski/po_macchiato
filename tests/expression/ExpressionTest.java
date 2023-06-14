package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ModuloZeroException;
import org.junit.jupiter.api.Test;
import syntax.exceptions.UndefinedSymbolException;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    @Test
    void evaluateAndPrint() throws Exception {
        // ((((-1) + 2) * 4 % 3) + 24 / 2) / 3 = 4
        Expression e = Division.of(
                Addition.of(
                        Modulo.of(
                                Multiplication.of(
                                        Addition.of(Constant.of(-1), Constant.of(2)),
                                        Constant.of(4)
                                ),
                                Constant.of(3)
                        ),
                        Division.of(Constant.of(24), Constant.of(2))
                ),
                Constant.of(3)
        );
        assertEquals(4, e.evaluate(null));
        assertEquals("((((-1) + 2) * 4 % 3) + 24 / 2) / 3", e.toString());
    }
    @Test
    void divisionByZero() throws Exception {
        Expression e = Division.of(Constant.of(20), Addition.of(Constant.of(-3), Constant.of(3)));
        assertThrows(DivisionByZeroException.class, () -> e.evaluate(null));
        try {
            e.evaluate(null);
        }catch(DivisionByZeroException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    void modulo() throws Exception {
        Expression e1 = Addition.of(Constant.of(1), Modulo.of(Constant.of(4), Constant.of(0)));
        Expression e2 = Addition.of(Constant.of(1), Modulo.of(Constant.of(4), Constant.of(2)));
        assertThrows(ModuloZeroException.class, () -> e1.evaluate(null));
        assertEquals(1, e2.evaluate(null));

        try {
            e1.evaluate(null);
        }catch(ModuloZeroException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    void noArgument() throws Exception {
        Expression e = Subtraction.of(Constant.of(20), Variable.named('a'));
        assertThrows(UndefinedSymbolException.class, () -> e.evaluate(null));
        try {
            e.evaluate(null);
        }catch(UndefinedSymbolException ex) {
            System.out.println(ex.getMessage());
        }
    }
}