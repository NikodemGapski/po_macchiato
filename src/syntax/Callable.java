package syntax;

import expression.Expression;
import expression.exceptions.ExpressionException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public abstract class Callable {
    public abstract void execute(Scope scope) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException;
    public void debug(Scope scope, Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        // got here by the step command
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;
        execute(scope);
    }

    protected int evaluateAndCatch(Expression expression, Scope scope) throws UndefinedVariableException, ExpressionArithmeticException {
        try {
            return expression.evaluate(scope);
        }catch(UndefinedVariableException e) {
            throw new UndefinedVariableException(e.getName(), toString(), scope.getVisibleVariables());
        }catch(ExpressionException e) {
            throw new ExpressionArithmeticException(e, toString(), scope.getVisibleVariables());
        }
    }
    @Override
    public abstract String toString();
}
