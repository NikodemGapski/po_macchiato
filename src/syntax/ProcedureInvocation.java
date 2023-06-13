package syntax;

import expression.Expression;
import syntax.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcedureInvocation extends Instruction {
    private final String name;
    private final List<Expression> params;
    public ProcedureInvocation(String name, List<Expression> params) throws NullArgumentException {
        if(name == null || params == null) throw new NullArgumentException();

        this.name = name;
        this.params = params;
    }
    @Override
    public void execute(Scope scope) throws ExpressionArithmeticException, RepeatedDeclarationException, UndefinedSymbolException, InvalidParamCountException {
        // evaluate parameters
        List<Integer> values = evaluateParams(scope);
        // find and execute the procedure
        Procedure p = findProcedure(name, scope);
        try {
            p.execute(values, scope);
        }catch(InvalidParamCountException e) {
            catchInvalidParamCountException(e, scope);
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
        Procedure p = findProcedure(name, scope);
        try {
            p.debug(values, scope, debugger);
        }catch(InvalidParamCountException e) {
            catchInvalidParamCountException(e, scope);
        }
    }
    private List<Integer> evaluateParams(Scope scope) throws ExpressionArithmeticException, UndefinedSymbolException {
        List<Integer> values = new ArrayList<>();
        for(Expression e : params) {
            values.add(evaluateAndCatch(e, scope));
        }
        return values;
    }
    private Procedure findProcedure(String name, Scope scope) throws UndefinedSymbolException {
        try {
            return scope.getProcedure(name);
        }catch(UndefinedSymbolException e) {
            throw new UndefinedSymbolException(name, toString(), scope.getVisibleVariables());
        }
    }
    private void catchInvalidParamCountException(InvalidParamCountException e, Scope scope) throws InvalidParamCountException {
        if(e.getInstruction() != null) throw e;
        throw new InvalidParamCountException(e, toString(), scope.getVisibleVariables());
    }
    @Override
    public String toString() {
        return "call " + name + '(' + params.stream().map(Object::toString).collect(Collectors.joining(", ")) + ");";
    }
}
