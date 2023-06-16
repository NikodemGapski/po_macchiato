package syntax;

import syntax.exceptions.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Procedure {
    private final String name;
    private final List<Character> params;
    private final Instruction instruction;
    public Procedure(String name, List<Character> params, Instruction instruction) throws NullArgumentException, InvalidProcedureNameException {
        if(name == null || params == null || instruction == null) throw new NullArgumentException();
        if(!name.matches("[a-z]+")) throw new InvalidProcedureNameException(name);

        this.name = name;
        this.params = params;
        this.instruction = instruction;
    }
    public void execute(List<Integer> values, Scope scope) throws ExpressionArithmeticException, RepeatedDeclarationException, UndefinedSymbolException, InvalidParamCountException {
        // assign argument values to params
        Scope innerScope = createScope(values, scope);
        // execute the code
        instruction.execute(innerScope);
    }
    public void debug(List<Integer> values, Scope scope, Debugger debugger) throws ExpressionArithmeticException, RepeatedDeclarationException, UndefinedSymbolException, InvalidParamCountException {
        // got here by the step command
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;
        // assign argument value to params
        Scope innerScope = createScope(values, scope);
        // execute the code
        if(debugger.isContinue()) instruction.execute(innerScope);
        else instruction.debug(innerScope, debugger);
    }
    private Scope createScope(List<Integer> values, Scope scope) throws RepeatedDeclarationException, InvalidParamCountException {
        if(values.size() != params.size()) throw new InvalidParamCountException(values.size(), params.size());
        Scope innerScope = new Scope(scope);
        Iterator<Character> param = params.iterator();
        Iterator<Integer> value = values.iterator();
        while(param.hasNext() && value.hasNext()) {
            innerScope.declareVariable(param.next(), value.next());
        }
        return innerScope;
    }
    public String getName() {
        return name;
    }
    public int paramCount() {
        return params.size();
    }
    public String toString() {
        return name + '(' + params.stream().map(Object::toString).collect(Collectors.joining(", ")) + ')';
    }
}
