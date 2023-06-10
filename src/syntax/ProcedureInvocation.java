package syntax;

import expression.Expression;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidParamCountException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcedureInvocation extends Instruction {
    private final String name;
    private final List<Expression> params;
    public ProcedureInvocation(String name, List<Expression> params) {
        this.name = name;
        this.params = params;
    }
    @Override
    public void execute(Scope scope) throws ExpressionArithmeticException, RepeatedDeclarationException, UndefinedSymbolException, InvalidParamCountException {
        // evaluate parameters
        List<Integer> values = evaluateParams(scope);
        // find and execute the procedure
        try {
            scope.getProcedure(name).execute(values, scope);
        }catch(UndefinedSymbolException e) {
            throw new UndefinedSymbolException(name, toString(), scope.getVisibleVariables());
        }catch(InvalidParamCountException e) {
            throw new InvalidParamCountException(e, toString(), scope.getVisibleVariables());
        }
    }
    @Override
    public void debug(Scope scope, Debugger debugger) throws ExpressionArithmeticException, RepeatedDeclarationException, UndefinedSymbolException, InvalidParamCountException {
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;
        if(debugger.isContinue()) {
            execute(scope);
            return;
        }
        // evaluate parameters
        List<Integer> values = evaluateParams(scope);
        // find and debug the procedure
        try {
            scope.getProcedure(name).debug(values, scope, debugger);
        }catch(UndefinedSymbolException e) {
            throw new UndefinedSymbolException(name, toString(), scope.getVisibleVariables());
        }catch(InvalidParamCountException e) {
            throw new InvalidParamCountException(e, toString(), scope.getVisibleVariables());
        }
    }
    private List<Integer> evaluateParams(Scope scope) throws ExpressionArithmeticException, UndefinedSymbolException {
        List<Integer> values = new ArrayList<>();
        for(Expression e : params) {
            values.add(evaluateAndCatch(e, scope));
        }
        return values;
    }
    @Override
    public String toString() {
        return "call " + name + '(' + params.stream().map(Object::toString).collect(Collectors.joining(", ")) + ");";
    }
}
