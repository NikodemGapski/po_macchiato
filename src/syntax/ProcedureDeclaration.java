package syntax;

import syntax.exceptions.NullArgumentException;
import syntax.exceptions.RepeatedDeclarationException;

public class ProcedureDeclaration extends Declaration {
    private final Procedure procedure;
    public ProcedureDeclaration(Procedure procedure) throws NullArgumentException {
        if(procedure == null) throw new NullArgumentException();

        this.procedure = procedure;
    }
    @Override
    public void execute(Scope scope) throws RepeatedDeclarationException {
        try {
            scope.declareProcedure(procedure);
        }catch(RepeatedDeclarationException e) {
            throw new RepeatedDeclarationException(procedure.getName(), toString(), scope.getVisibleVariables());
        }
    }
    @Override
    public String toString() {
        return "declare " + procedure + ';';
    }
}
