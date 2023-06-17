package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ExpressionException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public class Division extends Operation {
    public Division(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    public static Division of(Expression left, Expression right) throws NullArgumentException {
        return new Division(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException {
        int r = right.evaluate(scope);
        if(r == 0) throw new DivisionByZeroException();
        return left.evaluate(scope) / r;
    }
    @Override
    public int rank() {
        return 2;
    }
    @Override
    public String operator() {
        return "/";
    }
}
