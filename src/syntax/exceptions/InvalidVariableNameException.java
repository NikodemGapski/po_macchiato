package syntax.exceptions;

public class InvalidVariableNameException extends MacchiatoCompilationException {
    private final char name;
    public InvalidVariableNameException(char name) {
        this.name = name;
    }
    @Override
    public String getMessage() {
        return "Variable names must range from 'a' to 'z'! Tried to use name: " + name;
    }
}
