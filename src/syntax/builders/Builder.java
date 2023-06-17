package syntax.builders;

import syntax.Block;
import syntax.Declaration;
import syntax.Instruction;
import syntax.Macchiato;
import syntax.exceptions.DeclarationAfterInstructionException;
import syntax.exceptions.DoubleDeclarationSequenceException;
import syntax.exceptions.DoubleInstructionSequenceException;
import syntax.exceptions.NullArgumentException;

public class Builder {
    private DeclarationBuilder declarationBuilder;
    private InstructionBuilder instructionBuilder;
    public DeclarationBuilder beginDeclarations() throws DeclarationAfterInstructionException, DoubleDeclarationSequenceException {
        if(instructionBuilder != null) throw new DeclarationAfterInstructionException();
        if(declarationBuilder != null) throw new DoubleDeclarationSequenceException();
        declarationBuilder = new DeclarationBuilder(this);
        return declarationBuilder;
    }
    public InstructionBuilder beginInstructions() throws DoubleInstructionSequenceException {
        if(instructionBuilder != null) throw new DoubleInstructionSequenceException();
        instructionBuilder = new InstructionBuilder(this);
        return instructionBuilder;
    }
    public Block build() throws NullArgumentException {
        Declaration[] declarations = declarationBuilder == null ? new Declaration[0] : declarationBuilder.build();
        Instruction[] instructions = instructionBuilder == null ? new Instruction[0] : instructionBuilder.build();

        return new Block(declarations, instructions);
    }
    public Macchiato buildMacchiato() throws NullArgumentException {
        Block main = build();
        return new Macchiato(main);
    }
}
