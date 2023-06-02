package expression;

import expression.exceptions.ExpressionException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedVariableException;

public class Subtraction extends Operation {
    public Subtraction(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedVariableException {
        return left.evaluate(scope) - right.evaluate(scope);
    }
    @Override
    public int rank() {
        return 1;
    }
    @Override
    public String operator() {
        return "-";
    }
}
