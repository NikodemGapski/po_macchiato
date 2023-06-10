package syntax.exceptions;

public class InvalidParamCountException extends MacchiatoRuntimeException {
    private final int provided, required;
    public InvalidParamCountException(int provided, int required) {
        this.provided = provided;
        this.required = required;
    }
    public InvalidParamCountException(InvalidParamCountException cause, String instruction, String values) {
        super(instruction, values);
        this.provided = cause.provided;
        this.required = cause.required;
    }
    @Override
    protected String specialisedMessage() {
        return "Invalid parameter count! Provided: " + provided + ", required: " + required;
    }
}
