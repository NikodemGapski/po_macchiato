package syntax;

import expression.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import syntax.builders.Builder;
import syntax.builders.InstructionBuilder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildersTest {
    private ByteArrayOutputStream outputInterception;
    @BeforeEach
    public void init() {
        outputInterception = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputInterception));
    }
    @Test
    public void macchiatoOldMain() throws Exception {

        Macchiato m = new Builder()
                .beginDeclarations()
                .variable('n', new Constant(30))
                .endDeclarations()
                .beginInstructions()
                .forLoop('k', new Subtraction(new Variable('n'), new Constant(1)), new InstructionBuilder()
                        .block(new Builder()
                                .beginDeclarations()
                                .variable('p', new Constant(1))
                                .endDeclarations()
                                .beginInstructions()
                                .assign('k', new Addition(new Variable('k'), new Constant(2)))
                                .forLoop('i', new Subtraction(new Variable('k'), new Constant(2)), new InstructionBuilder()
                                        .assign('i', new Addition(new Variable('i'), new Constant(2)))
                                        .ifStatement(new Modulo(new Variable('k'), new Variable('i')), IfStatement.Type.Equal, new Constant(0), new InstructionBuilder()
                                                .assign('p', new Constant(0))
                                                .build()
                                        )
                                        .build()
                                )
                                .ifStatement(new Variable('p'), IfStatement.Type.Equal, new Constant(1), new InstructionBuilder()
                                        .print(new Variable('k'))
                                        .build()
                                )
                                .endInstructions().build()
                        )
                        .build()
                )
                .endInstructions()
                .buildMacchiato();
        m.execute();

        assertEquals(
                """
                        2
                        3
                        5
                        7
                        11
                        13
                        17
                        19
                        23
                        29
                        int n: 30""",
                outputInterception.toString().trim()
        );
    }
}
