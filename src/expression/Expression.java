package expression;

import expression.exceptions.ExpressionException;
import syntax.Scope;
import syntax.exceptions.UndefinedSymbolException;

public interface Expression {
    // evaluate the expression in a given scope
    int evaluate(Scope scope) throws ExpressionException, UndefinedSymbolException;
    // the expression rank; the larger the rank, the less likely it is
    // that the expression will be enclosed in parentheses
    int rank();
}
