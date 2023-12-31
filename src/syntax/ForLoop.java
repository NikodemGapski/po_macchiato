package syntax;

import expression.Constant;
import expression.Expression;
import syntax.exceptions.*;

public class ForLoop extends ScopeInstruction {
    private final Expression repeatCount;
    private final VariableDeclaration variableDeclaration;
    private final Instruction[] instructions;
    public ForLoop(char variableName, Expression repeatCount, Instruction[] instructions) throws InvalidVariableNameException, NullArgumentException {
        if(variableName < 'a' || variableName > 'z') throw new InvalidVariableNameException(variableName);
        if(repeatCount == null || instructions == null) throw new NullArgumentException();

        this.repeatCount = repeatCount;
        variableDeclaration = new VariableDeclaration(variableName, new Constant(0));
        this.instructions = instructions;
        for(Instruction i : instructions) {
            if(i == null) throw new NullArgumentException();
        }
    }
    @Override
    public void execute(Scope scope) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException {
        int count = evaluateAndCatch(repeatCount, scope);
        Scope innerScope = new Scope(scope);
        variableDeclaration.execute(innerScope);

        for(int i = 0; i < count; ++i) {
            innerScope.setVariable(variableDeclaration.getName(), i);
            for(Instruction instruction : instructions) {
                instruction.execute(innerScope);
            }
        }
    }
    @Override
    public void debug(Scope scope, Debugger debugger) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException {
        // got here by the step command
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;

        int count = evaluateAndCatch(repeatCount, scope);
        Scope innerScope = new Scope(scope);
        variableDeclaration.execute(innerScope);

        // execute body instructions
        for(int i = 0; i < count; ++i) {
            // got here by the step OR continue command
            if(i > 0 && debugger.moveStepAndCheckExit(toString(), innerScope)) return;
            innerScope.setVariable(variableDeclaration.getName(), i);

            for(Instruction instruction : instructions) {
                if(debugger.isContinue()) {
                    instruction.execute(innerScope);
                }else {
                    instruction.debug(innerScope, debugger);
                    if(debugger.isExit()) return;
                }
            }
        }

        debugger.handleStep(endString(), innerScope);
        if(debugger.isStep()) debugger.moveStep();
    }
    @Override
    public String toString() {
        char c = variableDeclaration.getName();
        return "for(int " + c + " = 0; " + c + " < " + repeatCount + "; ++" + c + ") {";
    }
    @Override
    public String endString() {
        return "end for }";
    }
}
