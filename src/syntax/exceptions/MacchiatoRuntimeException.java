package syntax.exceptions;

public abstract class MacchiatoRuntimeException extends Exception {
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
    @Override
    public String getMessage() {
        return
                specialisedMessage() + '\n'
                + "at instruction: " + instruction + '\n'
                + "Visible variables:\n" + values;
    }
    protected abstract String specialisedMessage();
}
