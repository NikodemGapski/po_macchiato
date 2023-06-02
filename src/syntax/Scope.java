package syntax;

import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public interface Scope {
    void setVariable(char c, int value) throws UndefinedVariableException;
    int getVariable(char c) throws UndefinedVariableException;
    String getVisibleVariables();
    String getVisibleVariables(int scopeHeight);
    String endString();
}
