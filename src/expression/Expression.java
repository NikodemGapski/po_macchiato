package expression;

import expression.exceptions.ExpressionException;
import syntax.Scope;
import syntax.exceptions.UndefinedVariableException;

public interface Expression {
    // evaluate the expression in a given scope
    int evaluate(Scope scope) throws ExpressionException, UndefinedVariableException;
    // the expression rank; the larger the rank, the less likely it is
    // that the expression will be enclosed in parentheses
    int rank();
}
