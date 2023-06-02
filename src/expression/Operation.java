package expression;

import syntax.exceptions.NullArgumentException;

import java.util.Objects;

public abstract class Operation implements Expression {
    protected final Expression left, right;
    public Operation(Expression left, Expression right) throws NullArgumentException {
        if(left == null || right == null) throw new NullArgumentException();
        this.left = left;
        this.right = right;
    }
    protected abstract String operator();
    @Override
    public String toString() {
        String l = left.toString(), r = right.toString();
        if(left.rank() < rank()) {
            l = "(" + l + ")";
        }else if(right.rank() < rank() || (Objects.equals(operator(), "/") && right.rank() <= rank())) {
            r = "(" + r + ")";
        }
        return l + " " + operator() + " " + r;
    }
}
