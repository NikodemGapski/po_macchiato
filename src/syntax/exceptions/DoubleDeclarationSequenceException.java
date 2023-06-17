package syntax.exceptions;

public class DoubleDeclarationSequenceException extends MacchiatoCompilationException {
    @Override
    public String getMessage() {
        return ".beginDeclarations() called twice! There can only be one sequence of declarations in a block.";
    }
}
