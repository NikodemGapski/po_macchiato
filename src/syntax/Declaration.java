package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public class Declaration extends Callable {
    private final char name;
    private final Expression expression;
    public Declaration(char name, Expression expression) throws InvalidVariableNameException, NullArgumentException {
        if(name < 'a' || name > 'z') throw new InvalidVariableNameException(name);
        if(expression == null) throw new NullArgumentException();

        this.name = name;
        this.expression = expression;
    }
    @Override
    public void execute(Scope scope) throws RepeatedDeclarationException, ExpressionArithmeticException, UndefinedVariableException {
        try {
            scope.declareVariable(name, evaluateAndCatch(expression, scope));
        }catch(RepeatedDeclarationException e) {
            throw new RepeatedDeclarationException(name, toString(), scope.getVisibleVariables());
        }
    }
    public char getName() {
        return name;
    }
    @Override
    public String toString() {
        return "int " + name + " = " + expression + ';';
    }
}
