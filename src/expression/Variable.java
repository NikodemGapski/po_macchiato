package expression;

import syntax.Scope;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.UndefinedSymbolException;

public class Variable implements Expression {
    private final char name;
    public Variable(char name) throws InvalidVariableNameException {
        if(name < 'a' || name > 'z') throw new InvalidVariableNameException(name);
        this.name = name;
    }
    @Override
    public int evaluate(Scope scope) throws UndefinedSymbolException {
        if(scope == null) throw new UndefinedSymbolException(Character.toString(name));
        return scope.getVariable(name);
    }
    @Override
    public int rank() {
        return 100;
    }
    @Override
    public String toString() {
        return Character.toString(name);
    }
}
