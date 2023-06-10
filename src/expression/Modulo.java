package expression;

import expression.exceptions.ExpressionException;
import expression.exceptions.ModuloZeroException;
import syntax.exceptions.NullArgumentException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public class Modulo extends Operation {
    public Modulo(Expression left, Expression right) throws NullArgumentException {
        super(left, right);
    }
    @Override
    public int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException {
        int r = right.evaluate(scope);
        if(r == 0) throw new ModuloZeroException();
        return left.evaluate(scope) % r;
    }
    @Override
    public int rank() {
        return 0;
    }
    @Override
    public String operator() {
        return "%";
    }
}
