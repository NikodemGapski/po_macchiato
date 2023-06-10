package syntax;

import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidParamCountException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

public abstract class ScopeInstruction extends Instruction {
    @Override
    public abstract void debug(Scope scope, Debugger debugger) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException;
    public abstract String endString();
}
