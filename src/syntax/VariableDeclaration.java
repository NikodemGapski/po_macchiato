package syntax;

import expression.Expression;
import syntax.exceptions.NullArgumentException;
import syntax.exceptions.ExpressionArithmeticException;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

public class VariableDeclaration extends Declaration {
    private final char name;
    private final Expression expression;
    public VariableDeclaration(char name, Expression expression) throws InvalidVariableNameException, NullArgumentException {
        if(name < 'a' || name > 'z') throw new InvalidVariableNameException(name);
        if(expression == null) throw new NullArgumentException();

        this.name = name;
        this.expression = expression;
    }
    @Override
    public void execute(Scope scope) throws RepeatedDeclarationException, ExpressionArithmeticException, UndefinedSymbolException {
        try {
            scope.declareVariable(name, evaluateAndCatch(expression, scope));
        }catch(RepeatedDeclarationException e) {
            throw new RepeatedDeclarationException(String.valueOf(name), toString(), scope.getVisibleVariables());
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
