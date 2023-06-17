package syntax.builders;

import expression.Expression;
import syntax.*;
import syntax.exceptions.InvalidVariableNameException;
import syntax.exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

public class InstructionBuilder {
    private final List<Instruction> instructions;
    private final Builder parent;
    public InstructionBuilder() {
        parent = null;
        instructions = new ArrayList<>();
    }
    public InstructionBuilder(Builder parent) {
        this.parent = parent;
        instructions = new ArrayList<>();
    }
    public InstructionBuilder assign(char name, Expression expression) throws InvalidVariableNameException, NullArgumentException {
        instructions.add(new Assignment(name, expression));
        return this;
    }
    public InstructionBuilder block(Block block) throws NullArgumentException {
        if(block == null) throw new NullArgumentException();
        instructions.add(block);
        return this;
    }
    public InstructionBuilder forLoop(char variableName, Expression repeatCount, Instruction[] instructions) throws InvalidVariableNameException, NullArgumentException {
        this.instructions.add(new ForLoop(variableName, repeatCount, instructions));
        return this;
    }
    public InstructionBuilder ifStatement(Expression left, IfStatement.Type type, Expression right, Instruction[] then) throws NullArgumentException {
        instructions.add(new IfStatement(left, type, right, then));
        return this;
    }
    public InstructionBuilder ifStatement(Expression left, IfStatement.Type type, Expression right, Instruction[] then, Instruction[] otherwise) throws NullArgumentException {
        instructions.add(new IfStatement(left, type, right, then, otherwise));
        return this;
    }
    public InstructionBuilder invoke(String name, List<Expression> params) throws NullArgumentException {
        instructions.add(new ProcedureInvocation(name, params));
        return this;
    }
    public InstructionBuilder print(Expression expression) throws NullArgumentException {
        instructions.add(new Print(expression));
        return this;
    }
    public Builder endInstructions() {
        return parent;
    }
    public Instruction[] build() {
        return instructions.toArray(new Instruction[0]);
    }
}
