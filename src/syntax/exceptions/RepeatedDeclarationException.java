package syntax.exceptions;

public class RepeatedDeclarationException extends MacchiatoRuntimeException {
    private char name;
    public RepeatedDeclarationException() {}
    public RepeatedDeclarationException(char name, String instruction, String values) {
        super(instruction, values);
        this.name = name;
    }
    @Override
    protected String specialisedMessage() {
        return "Variable " + name + " declared twice!";
    }
}
