package syntax;

import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public class Block extends ScopeInstruction {
    private final Callable[] instructions;
    protected final Integer[] variables;
    public Block(Declaration[] declarations, Instruction[] instructions) throws NullArgumentException {
        if(declarations == null || instructions == null) throw new NullArgumentException();

        // organise declarations and instructions
        this.instructions = new Callable[declarations.length + instructions.length];
        System.arraycopy(declarations, 0, this.instructions, 0, declarations.length);
        for(int i = declarations.length; i - declarations.length < instructions.length; ++i) {
            this.instructions[i] = instructions[i - declarations.length];
        }
        // check for null callables
        for(Callable c : this.instructions) {
            if(c == null) throw new NullArgumentException();
        }
        // initialise variables
        variables = new Integer['z' - 'a' + 1];
    }
    @Override
    public void execute(Scope scope) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        Scope innerScope = new Scope(scope);
        executeBody(innerScope);
    }
    public Scope execute() throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        Scope innerScope = new Scope();
        executeBody(innerScope);
        return innerScope;
    }
    private void executeBody(Scope innerScope) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        for(Callable instruction : instructions) {
            instruction.execute(innerScope);
        }
    }
    @Override
    public void debug(Scope scope, Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;
        Scope innerScope = new Scope(scope);
        debugBody(innerScope, debugger);
    }
    public Scope debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        // got here by the step command
        Scope innerScope = new Scope();
        if(debugger.moveStepAndCheckExit(toString(), innerScope)) return innerScope;
        debugBody(innerScope, debugger);
        return innerScope;
    }
    private void debugBody(Scope innerScope, Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        // execute body instructions
        for(Callable instruction : instructions) {
            if(debugger.isContinue()) {
                instruction.execute(innerScope);
            }else {
                instruction.debug(innerScope, debugger);
                if(debugger.isExit()) return;
            }
        }

        debugger.handleStep(endString(), innerScope);
        if(debugger.isStep()) debugger.moveStep();
    }
    @Override
    public String toString() {
        return "begin block {";
    }
    @Override
    public String endString() {
        return "end block }";
    }
}
