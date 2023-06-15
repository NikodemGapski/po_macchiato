package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ExpressionException;
import expression.exceptions.ModuloZeroException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import syntax.Scope;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    private static Scope scope;
    @BeforeAll
    static void setScope() throws RepeatedDeclarationException {
        scope = new Scope();
        scope.declareVariable('a', 2);
        scope.declareVariable('b', 3);
    }
    @Test
    void constant() throws UndefinedSymbolException, ExpressionException {
        Expression e = Constant.of(2);
        assertEquals(2, e.evaluate(scope));
    }
    @Test
    void addition() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Addition.of(Constant.of(2), Constant.of(3));
        assertEquals(2 + 3, e.evaluate(scope));
    }
    @Test
    void subtraction() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Subtraction.of(Constant.of(2), Constant.of(3));
        assertEquals(2 - 3, e.evaluate(scope));
    }
    @Test
    void multiplication() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Multiplication.of(Constant.of(2), Constant.of(3));
        assertEquals(2 * 3, e.evaluate(scope));
    }
    @Test
    void division() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Division.of(Constant.of(7), Constant.of(3));
        assertEquals(7 / 3, e.evaluate(scope));
    }
    @Test
    void modulo() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Modulo.of(Constant.of(7), Constant.of(3));
        assertEquals(7 % 3, e.evaluate(scope));
    }
    @Test
    void variable() throws UndefinedSymbolException, ExpressionException, InvalidVariableNameException {
        Expression e = Variable.named('b');
        assertEquals(scope.getVariable('b'), e.evaluate(scope));
    }
    @Test
    void print() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
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
        assertEquals(4, e.evaluate(scope));
        assertEquals("((((-1) + 2) * 4 % 3) + 24 / 2) / 3", e.toString());
    }
    @Test
    void divisionByZero() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Division.of(Constant.of(2), Constant.of(0));
        assertThrows(DivisionByZeroException.class, () -> e.evaluate(scope));
        try {
            e.evaluate(scope);
        }catch(DivisionByZeroException ex) {
            assertEquals("Cannot divide by zero!", ex.getMessage());
        }
    }
    @Test
    void moduloZero() throws UndefinedSymbolException, ExpressionException, NullArgumentException {
        Expression e = Modulo.of(Constant.of(2), Constant.of(0));
        assertThrows(ModuloZeroException.class, () -> e.evaluate(scope));
        try {
            e.evaluate(scope);
        }catch(ModuloZeroException ex) {
            assertEquals("Cannot take modulo zero!", ex.getMessage());
        }
    }
    @Test
    void invalidName() {
        assertThrows(InvalidVariableNameException.class, () -> Variable.named('A'));
    }
}