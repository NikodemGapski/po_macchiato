package expression;

import syntax.Scope;

public class Constant implements Expression {
    private final int value;
    public Constant(int value) {
        this.value = value;
    }
    @Override
    public int evaluate(Scope scope) {
        return value;
    }
    @Override
    public int rank() {
        return value < 0 ? 0 : 100;
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
