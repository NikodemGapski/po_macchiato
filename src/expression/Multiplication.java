package expression;

import expression.exceptions.ExpressionException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public class Multiplication extends Operation {
    public Multiplication(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException {
        return left.evaluate(scope) * right.evaluate(scope);
    }
    @Override
    public int rank() {
        return 2;
    }
    @Override
    public String operator() {
        return "*";
    }
}
