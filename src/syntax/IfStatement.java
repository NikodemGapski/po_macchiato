package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public class IfStatement extends ScopeInstruction {
    private final Expression left, right;
    public enum Type {
        Less,
        LessEqual,
        Equal,
        GreaterEqual,
        Greater,
        NotEqual
    }
    private final Type type;
    private final Instruction[] then;
    private final Instruction[] otherwise;
    public IfStatement(Expression left, Type type, Expression right, Instruction[] then) throws NullArgumentException {
        this(left, type, right, then, null);
    }
    public IfStatement(Expression left, Type type, Expression right, Instruction[] then, Instruction[] otherwise) throws NullArgumentException {
        if(left == null || type == null || right == null || then == null) throw new NullArgumentException();

        this.left = left;
        this.right = right;
        this.type = type;
        this.then = then;
        this.otherwise = otherwise;
        for(Instruction i : then) {
            if(i == null) throw new NullArgumentException();
            i.setScope(this);
        }
        if(otherwise != null) {
            for(Instruction i : otherwise) {
                if(i == null) throw new NullArgumentException();
                i.setScope(this);
            }
        }
    }
    @Override
    public void execute() throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        int l = evaluateAndCatch(left, this), r = evaluateAndCatch(right, this);
        if(checkCondition(l, r)) {
            for(Instruction instruction : then) {
                instruction.execute();
            }
        }else if(otherwise != null) {
            for(Instruction instruction : otherwise) {
                instruction.execute();
            }
        }
    }
    @Override
    public void debug(Debugger debugger) throws UndefinedVariableException, ExpressionArithmeticException, RepeatedDeclarationException {
        // got here by the step command
        if(moveStepAndCheckExit(debugger)) return;

        int l = evaluateAndCatch(left, this), r = evaluateAndCatch(right, this);

        // execute body instructions
        if(checkCondition(l, r)) {
            for(Instruction instruction : then) {
                if(debugger.isContinue()) {
                    instruction.execute();
                }else {
                    instruction.debug(debugger);
                    if(debugger.isExit()) return;
                }
            }
        }else if(otherwise != null) {
            debugger.handleStep("else {", this);
            if(debugger.isExit()) return;
            if(debugger.isStep()) debugger.moveStep();

            for(Instruction instruction : otherwise) {
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
        scope.setVariable(c, value);
    }
    @Override
    public int getVariable(char c) throws UndefinedVariableException {
        return scope.getVariable(c);
    }
    // If statement-specific implementation, as if statement doesn't introduce any new variables, thus it is not treated as scope by the user.
    @Override
    public String getVisibleVariables(int scopeHeight) {
        return scope.getVisibleVariables(scopeHeight);
    }
    private boolean checkCondition(int l, int r) {
        return switch (type) {
            case Less -> l < r;
            case LessEqual -> l <= r;
            case Equal -> l == r;
            case GreaterEqual -> l >= r;
            case Greater -> l > r;
            case NotEqual -> l != r;
        };
    }
    private String conditionString() {
        return switch (type) {
            case Less -> "<";
            case LessEqual -> "<=";
            case Equal -> "==";
            case GreaterEqual -> ">=";
            case Greater -> ">";
            case NotEqual -> "!=";
        };
    }
    @Override
    public void resetRecursive() {
        for(Instruction instruction : then) {
            instruction.resetRecursive();
        }
        if(otherwise == null) return;
        for(Instruction instruction : otherwise) {
            instruction.resetRecursive();
        }
    }
    @Override
    public String toString() {
        return "if(" + left + ' ' + conditionString() + ' ' + right + ") {";
    }
    @Override
    public String endString() {
        return "end if }";
    }
}
