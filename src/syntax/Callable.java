package syntax;

import expression.Expression;
import expression.exceptions.ExpressionException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public abstract class Callable {
    protected Scope scope;
    public void setScope(Scope scope) {
        this.scope = scope;
    }
    public abstract void execute() throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException;
    public void debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        // got here by the step command
        if(moveStepAndCheckExit(debugger)) return;
        execute();
    }
    // Handle the current step, following displays,
    // and move step if the next command is step.
    // Returns true if the next command is exit, false otherwise.
    protected boolean moveStepAndCheckExit(Debugger debugger) {
        debugger.handleStep(toString(), scope);
        if(debugger.isExit()) return true;
        if(debugger.isStep()) debugger.moveStep();
        return false;
    }
    protected int evaluateAndCatch(Expression expression) throws UndefinedVariableException, ExpressionArithmeticException {
        try {
            return expression.evaluate(scope);
        }catch(UndefinedVariableException e) {
            throw new UndefinedVariableException(e.getName(), toString(), scope.getVisibleVariables());
        }catch(ExpressionException e) {
            throw new ExpressionArithmeticException(e, toString(), scope.getVisibleVariables());
        }
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
    // Reset all values in this callable, and in the inside callables if this is a scope instruction.
    public void resetRecursive() {}
    @Override
    public abstract String toString();
}
