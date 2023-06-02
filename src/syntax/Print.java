package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.UndefinedVariableException;

public class Print extends Instruction {
    private final Expression expression;
    public Print(Expression expression) throws NullArgumentException {
        if(expression == null) throw new NullArgumentException();

        this.expression = expression;
    }
    @Override
    public void execute() throws UndefinedVariableException, ExpressionArithmeticException {
        System.out.println(evaluateAndCatch(expression));
    }
    @Override
    public String toString() {
        return "print(" + expression + ");";
    }
}
