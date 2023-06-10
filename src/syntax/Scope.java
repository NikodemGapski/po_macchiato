package syntax;

import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedVariableException;

public class Scope {
    private final Integer[] variables;
    private final Scope parent;
    public Scope() {
        variables = new Integer['z' - 'a' + 1];
        parent = null;
    }
    public Scope(Scope parent) {
        variables = new Integer['z' - 'a' + 1];
        this.parent = parent;
    }
    public void declareVariable(char c, int value) throws RepeatedDeclarationException {
        if(variables[c - 'a'] != null) {
            throw new RepeatedDeclarationException();
        }
        variables[c - 'a'] = value;
    }
    public void setVariable(char c, int value) throws UndefinedVariableException {
        if(variables[c - 'a'] == null) {
            if(parent == null) {
                throw new UndefinedVariableException();
            }
            parent.setVariable(c, value);
            return;
        }
        variables[c - 'a'] = value;
    }
    public int getVariable(char c) throws UndefinedVariableException {
        if(variables[c - 'a'] == null) {
            if(parent == null) {
                throw new UndefinedVariableException();
            }
            return parent.getVariable(c);
        }
        return variables[c - 'a'];
    }
    public String getVisibleVariables() {
        StringBuilder builder = new StringBuilder();
        for(char c = 'a'; c <= 'z'; ++c) {
            try {
                int value = getVariable(c);
                builder.append("int ").append(c).append(": ").append(value).append('\n');
            }catch (UndefinedVariableException ignored) {}
        }
        if(builder.isEmpty()) {
            return "No variables declared.";
        }
        return builder.toString().strip();
    }
    public String getVisibleVariables(int scopeHeight) {
        if(scopeHeight < 0) return null;
        if(scopeHeight > 0 && parent == null) return null;
        if(scopeHeight > 0) return parent.getVisibleVariables(scopeHeight - 1);
        return getVisibleVariables();
    }
}
