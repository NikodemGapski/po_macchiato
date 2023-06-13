package syntax.exceptions;

public class DoubleInstructionSequenceException extends MacchiatoCompilationException {
    @Override
    public String getMessage() {
        return ".beginInstructions() called twice! There can only be one sequence of instructions in a block.";
    }
}
