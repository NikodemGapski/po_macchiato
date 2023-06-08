package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.UndefinedVariableException;

public class Assignment extends Instruction {
    private final Expression expression;
    private final char name;
    public Assignment(char name, Expression expression) throws InvalidVariableNameException, NullArgumentException {
        if(name < 'a' || name > 'z') throw new InvalidVariableNameException(name);
        if(expression == null) throw new NullArgumentException();

        this.expression = expression;
        this.name = name;
    }
    @Override
    public void execute(Scope scope) throws UndefinedVariableException, ExpressionArithmeticException {
        try {
            scope.setVariable(name, evaluateAndCatch(expression, scope));
        }catch(UndefinedVariableException e) {
            throw new UndefinedVariableException(e.getName() == null ? name : e.getName(), toString(), scope.getVisibleVariables());
        }
    }
    @Override
    public String toString() {
        return name + " = " + expression + ';';
    }
}