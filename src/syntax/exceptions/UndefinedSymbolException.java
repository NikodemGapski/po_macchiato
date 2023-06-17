package syntax.exceptions;

public class UndefinedSymbolException extends MacchiatoRuntimeException {
    private final String name;
    public UndefinedSymbolException(String name) {
        this.name = name;
    }
    public UndefinedSymbolException(String name, String instruction, String values) {
        super(instruction, values);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    protected String specialisedMessage() {
        return "Symbol " + name + " undefined!";
    }
}
