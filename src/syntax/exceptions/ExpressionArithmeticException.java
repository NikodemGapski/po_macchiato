package syntax.exceptions;

import expression.exceptions.ExpressionException;

public class ExpressionArithmeticException extends MacchiatoRuntimeException {
    private final ExpressionException cause;
    public ExpressionArithmeticException(ExpressionException cause, String instruction, String values) {
        super(instruction, values);
        this.cause = cause;
    }
    @Override
    public String specialisedMessage() {
        return "Expression exception: " + cause.getMessage();
    }
}
