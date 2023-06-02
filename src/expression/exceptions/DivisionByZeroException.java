package expression.exceptions;

public class DivisionByZeroException extends ExpressionException {
    @Override
    public String getMessage() {
        return "Cannot divide by zero!";
    }
}
