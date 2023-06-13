package syntax.builders;

import expression.Expression;
import syntax.*;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

public class DeclarationBuilder {
    private final List<Declaration> declarations;
    private final Builder parent;
    public DeclarationBuilder(Builder parent) {
        this.parent = parent;
        declarations = new ArrayList<>();
    }
    public DeclarationBuilder variable(char name, Expression expression) throws InvalidVariableNameException, NullArgumentException {
        declarations.add(new VariableDeclaration(name, expression));
        return this;
    }
    public DeclarationBuilder procedure(String name, List<Character> params, Instruction instruction) throws NullArgumentException {
        declarations.add(new ProcedureDeclaration(new Procedure(name, params, instruction)));
        return this;
    }
    public Builder endDeclarations() {
        return parent;
    }
    public Declaration[] build() {
        return declarations.toArray(new Declaration[0]);
    }
}
