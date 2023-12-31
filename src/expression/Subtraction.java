package expression;

import expression.exceptions.ExpressionException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public class Subtraction extends Operation {
    public Subtraction(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    public static Subtraction of(Expression left, Expression right) throws NullArgumentException {
        return new Subtraction(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException {
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
