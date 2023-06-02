package syntax;

import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public abstract class ScopeInstruction extends Instruction implements Scope {
    @Override
    public abstract void debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException;
    @Override
    protected boolean moveStepAndCheckExit(Debugger debugger) {
        debugger.handleStep(toString(), this);
        if(debugger.isExit()) return true;
        if(debugger.isStep()) debugger.moveStep();
        return false;
    }
    @Override
    public String getVisibleVariables(int scopeHeight) {
        if(scopeHeight < 0) return null;
        if(scopeHeight > 0) return scope.getVisibleVariables(scopeHeight - 1);
        return getVisibleVariables();
    }
    @Override
    public String getVisibleVariables() {
        StringBuilder builder = new StringBuilder();
        for(char c = 'a'; c <= 'z'; ++c) {
            try {
                int value = getVariable(c);
                builder.append("int ").append(c).append(": ").append(value).append('\n');
            }catch (UndefinedVariableException ignored) {}
        }
        if(builder.isEmpty()) {
            return "No variables declared.";
        }
        return builder.toString().strip();
    }
    @Override
    public abstract void resetRecursive();
}
