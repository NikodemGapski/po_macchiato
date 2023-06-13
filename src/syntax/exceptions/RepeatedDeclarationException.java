package syntax.exceptions;

public class RepeatedDeclarationException extends MacchiatoRuntimeException {
    private String name;
    public RepeatedDeclarationException() {}
    public RepeatedDeclarationException(String name, String instruction, String values) {
        super(instruction, values);
        this.name = name;
    }
    @Override
    protected String specialisedMessage() {
        return "Variable " + name + " declared twice!";
    }
}
