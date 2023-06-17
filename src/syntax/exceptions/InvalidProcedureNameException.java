package syntax.exceptions;

public class InvalidProcedureNameException extends MacchiatoCompilationException {
    private final String name;
    public InvalidProcedureNameException(String name) {
        this.name = name;
    }
    @Override
    public String getMessage() {
        return "Procedure names can only contain letters from 'a' to 'z' and be non-empty! Tried to use name: " + name;
    }
}
