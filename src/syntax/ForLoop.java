package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public class ForLoop extends ScopeInstruction {
    private final Expression repeatCount;
    private final char variableName;
    private Integer variableValue;
    private final Instruction[] instructions;
    public ForLoop(char variableName, Expression repeatCount, Instruction[] instructions) throws InvalidVariableNameException, NullArgumentException {
        if(variableName < 'a' || variableName > 'z') throw new InvalidVariableNameException(variableName);
        if(repeatCount == null || instructions == null) throw new NullArgumentException();

        this.repeatCount = repeatCount;
        this.variableName = variableName;
        this.variableValue = null;
        this.instructions = instructions;
        for(Instruction i : instructions) {
            if(i == null) throw new NullArgumentException();
            i.setScope(this);
        }
    }
    @Override
    public void execute() throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        deleteLocals();
        int count = evaluateAndCatch(repeatCount);
        for(int i = 0; i < count; ++i) {
            variableValue = i;
            for(Instruction instruction : instructions) {
                instruction.execute();
            }
        }
    }
    @Override
    public void debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        deleteLocals();
        // got here by the step command
        if(moveStepAndCheckExit(debugger)) return;

        int count = evaluateAndCatch(repeatCount);
        variableValue = 0;
        // execute body instructions
        for(int i = 0; i < count; ++i) {
            // got here by the step OR continue command
            if(i > 0 && moveStepAndCheckExit(debugger)) return;
            variableValue = i;
            for(Instruction instruction : instructions) {
                if(debugger.isContinue()) {
                    instruction.execute();
                }else {
                    instruction.debug(debugger);
                    if(debugger.isExit()) return;
                }
            }
        }

        debugger.handleStep(endString(), this);
        if(debugger.isStep()) debugger.moveStep();
    }
    @Override
    public void setVariable(char c, int value) throws UndefinedVariableException {
        if(c != variableName) {
            scope.setVariable(c, value);
            return;
        }
        variableValue = value;
    }
    @Override
    public int getVariable(char c) throws UndefinedVariableException {
        if(c != variableName || variableValue == null) {
            return scope.getVariable(c);
        }
        return variableValue;
    }
    private void deleteLocals() {
        variableValue = null;
    }
    @Override
    public void resetRecursive() {
        deleteLocals();
        for(Instruction instruction : instructions) {
            instruction.resetRecursive();
        }
    }
    @Override
    public String toString() {
        return "for(int " + variableName + " = 0; " + variableName + " < " + repeatCount + "; ++" + variableName + ") {";
    }
    @Override
    public String endString() {
        return "end for }";
    }
}
