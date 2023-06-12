package syntax.exceptions;

public class UndefinedVariableException extends MacchiatoRuntimeException {
    private final Character name;
    public UndefinedVariableException(char name) {
        this.name = name;
    }
    public UndefinedVariableException(char name, String instruction, String values) {
        super(instruction, values);
        this.name = name;
    }
    public Character getName() {
        return name;
    }
    protected String specialisedMessage() {
        return "Variable " + name + " undefined!";
    }
}
