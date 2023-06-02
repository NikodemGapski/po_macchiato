package expression.exceptions;

public class ModuloZeroException extends ExpressionException {
    @Override
    public String getMessage() {
        return "Cannot take modulo zero!";
    }
}
