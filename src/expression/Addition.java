package expression;

import expression.exceptions.ExpressionException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public class Addition extends Operation {
    public Addition(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    public static Addition of(Expression left, Expression right) throws NullArgumentException {
        return new Addition(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException {
        return left.evaluate(scope) + right.evaluate(scope);
    }
    @Override
    public int rank() {
        return 1;
    }
    @Override
    public String operator() {
        return "+";
    }
}
