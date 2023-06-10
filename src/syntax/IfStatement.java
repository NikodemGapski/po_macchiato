package syntax;

import expression.Expression;
import syntax.exceptions.*;

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
        }
        if(otherwise != null) {
            for(Instruction i : otherwise) {
                if(i == null) throw new NullArgumentException();
            }
        }
    }
    @Override
    public void execute(Scope scope) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException {
        int l = evaluateAndCatch(left, scope), r = evaluateAndCatch(right, scope);
        Scope innerScope = new Scope(scope);

        if(checkCondition(l, r)) {
            for(Instruction instruction : then) {
                instruction.execute(innerScope);
            }
        }else if(otherwise != null) {
            for(Instruction instruction : otherwise) {
                instruction.execute(innerScope);
            }
        }
    }
    @Override
    public void debug(Scope scope, Debugger debugger) throws UndefinedSymbolException, ExpressionArithmeticException, RepeatedDeclarationException, InvalidParamCountException {
        // got here by the step command
        if(debugger.moveStepAndCheckExit(toString(), scope)) return;

        int l = evaluateAndCatch(left, scope), r = evaluateAndCatch(right, scope);
        Scope innerScope = new Scope(scope);

        // execute body instructions
        if(checkCondition(l, r)) {
            for(Instruction instruction : then) {
                if(debugger.isContinue()) {
                    instruction.execute(innerScope);
                }else {
                    instruction.debug(innerScope, debugger);
                    if(debugger.isExit()) return;
                }
            }
        }else if(otherwise != null) {
            debugger.handleStep("else {", scope);
            if(debugger.isExit()) return;
            if(debugger.isStep()) debugger.moveStep();

            for(Instruction instruction : otherwise) {
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
    public String toString() {
        return "if(" + left + ' ' + conditionString() + ' ' + right + ") {";
    }
    @Override
    public String endString() {
        return "end if }";
    }
}
