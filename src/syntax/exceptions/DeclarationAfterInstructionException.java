package syntax.exceptions;

public class DeclarationAfterInstructionException extends MacchiatoCompilationException {
    @Override
    public String getMessage() {
        return "Tried to add a declaration after an instruction. In Macchiato, all declarations must be added before any instruction in a given scope.";
    }
}
