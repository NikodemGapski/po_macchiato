package syntax;

import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

import java.util.Arrays;

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
        // set scopes and check for null callables
        for(Callable c : this.instructions) {
            if(c == null) throw new NullArgumentException();
            c.setScope(this);
        }
        for(Declaration d : declarations) {
            d.setBlockScope(this);
        }
        // initialise variables
        variables = new Integer['z' - 'a' + 1];
    }
    @Override
    public void execute() throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        deleteLocals();
        for(Callable instruction : instructions) {
            instruction.execute();
        }
    }
    @Override
    public void debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        deleteLocals();
        // got here by the step command
        if(moveStepAndCheckExit(debugger)) return;

        // execute body instructions
        for(Callable instruction : instructions) {
            if(debugger.isContinue()) {
                instruction.execute();
            }else {
                instruction.debug(debugger);
                if(debugger.isExit()) return;
            }
        }

        debugger.handleStep(endString(), this);
        if(debugger.isStep()) debugger.moveStep();
    }
    @Override
    public void setVariable(char c, int value) throws UndefinedVariableException {
        if(variables[c - 'a'] == null) {
            scope.setVariable(c, value);
            return;
        }
        variables[c - 'a'] = value;
    }
    @Override
    public int getVariable(char c) throws UndefinedVariableException {
        if(variables[c - 'a'] == null) {
            return scope.getVariable(c);
        }
        return variables[c - 'a'];
    }
    public void declareVariable(char c, int value) throws RepeatedDeclarationException {
        if(variables[c - 'a'] != null) {
            throw new RepeatedDeclarationException();
        }
        variables[c - 'a'] = value;
    }
    private void deleteLocals() {
        Arrays.fill(variables, null);
    }
    @Override
    public void resetRecursive() {
        deleteLocals();
        for(Callable instruction : instructions) {
            instruction.resetRecursive();
        }
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
