package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.UndefinedSymbolException;

public class Print extends Instruction {
    private final Expression expression;
    public Print(Expression expression) throws NullArgumentException {
        if(expression == null) throw new NullArgumentException();

        this.expression = expression;
    }
    @Override
    public void execute(Scope scope) throws UndefinedSymbolException, ExpressionArithmeticException {
        System.out.println(evaluateAndCatch(expression, scope));
    }
    @Override
    public String toString() {
        return "print(" + expression + ");";
    }
}
