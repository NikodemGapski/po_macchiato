package syntax;

import expression.Expression;
import expression.exceptions.ExpressionException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidParamCountException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

public abstract class Callable {
    public abstract void execute(Scope scope) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException;
    public void debug(Scope scope, Debugger debugger) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException {
        // got here by the step command
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;
        execute(scope);
    }

    protected int evaluateAndCatch(Expression expression, Scope scope) throws UndefinedSymbolException, ExpressionArithmeticException {
        try {
            return expression.evaluate(scope);
        }catch(UndefinedSymbolException e) {
            throw new UndefinedSymbolException(e.getName(), toString(), scope.getVisibleVariables());
        }catch(ExpressionException e) {
            throw new ExpressionArithmeticException(e, toString(), scope.getVisibleVariables());
        }
    }
    @Override
    public abstract String toString();
}
