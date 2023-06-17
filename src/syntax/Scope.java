package syntax;

import syntax.exceptions.RepeatedDeclarationException;
import syntax.exceptions.UndefinedSymbolException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Scope {
    private final Integer[] variables;
    private final HashMap<String, Procedure> procedures;
    private final Scope parent;
    public Scope() {
        variables = new Integer['z' - 'a' + 1];
        procedures = new HashMap<>();
        parent = null;
    }
    public Scope(Scope parent) {
        variables = new Integer['z' - 'a' + 1];
        procedures = new HashMap<>();
        this.parent = parent;
    }
    public void declareVariable(char c, int value) throws RepeatedDeclarationException {
        if(variables[c - 'a'] != null) {
            throw new RepeatedDeclarationException();
        }
        variables[c - 'a'] = value;
    }
    public void setVariable(char c, int value) throws UndefinedSymbolException {
        if(variables[c - 'a'] == null) {
            if(parent == null) {
                throw new UndefinedSymbolException(Character.toString(c));
            }
            parent.setVariable(c, value);
            return;
        }
        variables[c - 'a'] = value;
    }
    public int getVariable(char c) throws UndefinedSymbolException {
        if(variables[c - 'a'] == null) {
            if(parent == null) {
                throw new UndefinedSymbolException(Character.toString(c));
            }
            return parent.getVariable(c);
        }
        return variables[c - 'a'];
    }
    public void declareProcedure(Procedure procedure) throws RepeatedDeclarationException {
        if(procedures.containsKey(procedure.getName())) {
            throw new RepeatedDeclarationException();
        }
        procedures.put(procedure.getName(), procedure);
    }
    public Procedure getProcedure(String name) throws UndefinedSymbolException {
        if(!procedures.containsKey(name)) {
            if(parent == null) {
                throw new UndefinedSymbolException(name);
            }
            return parent.getProcedure(name);
        }
        return procedures.get(name);
    }
    public String getVisibleProcedures() {
        return getVisibleProcedures(new HashSet<>());
    }
    // Return a string of all visible procedures in this scope
    // except for procedures which names are in usedNames.
    public String getVisibleProcedures(Set<String> usedNames) {
        StringBuilder builder = new StringBuilder();
        for(String key : procedures.keySet()) {
            if(usedNames.contains(key)) continue;
            usedNames.add(key);
            builder.append(procedures.get(key)).append('\n');
        }
        return builder + (parent == null ? "" : parent.getVisibleProcedures(usedNames));
    }
    public String getVisibleVariables() {
        StringBuilder builder = new StringBuilder();
        for(char c = 'a'; c <= 'z'; ++c) {
            try {
                int value = getVariable(c);
                builder.append("int ").append(c).append(": ").append(value).append('\n');
            }catch (UndefinedSymbolException ignored) {}
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
