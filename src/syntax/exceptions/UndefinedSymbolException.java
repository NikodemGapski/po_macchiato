package syntax.exceptions;

public class UndefinedSymbolException extends MacchiatoRuntimeException {
    private String name;
    public UndefinedSymbolException() {}
    public UndefinedSymbolException(String name, String instruction, String values) {
        super(instruction, values);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    protected String specialisedMessage() {
        return "Variable " + name + " undefined!";
    }
}
