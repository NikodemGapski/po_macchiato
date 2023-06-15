package syntax.exceptions;

public abstract class MacchiatoRuntimeException extends MacchiatoException {
    private final String instruction;
    private final String values;
    public MacchiatoRuntimeException() {
        instruction = "";
        values = "";
    }
    public MacchiatoRuntimeException(String instruction, String values) {
        this.instruction = instruction;
        this.values = values;
    }
    public String getInstruction() {
        return instruction;
    }
    public String getValues() {
        return values;
    }

    @Override
    public String getMessage() {
        return
                specialisedMessage() + '\n'
                + "at instruction: " + instruction + '\n'
                + "Visible variables:\n" + values;
    }
    protected abstract String specialisedMessage();
}
