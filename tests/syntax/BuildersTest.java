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
                .variable('n', Constant.of(30))
                .endDeclarations()
                .beginInstructions()
                .forLoop('k', Subtraction.of(Variable.named('n'), Constant.of(1)), new InstructionBuilder()
                        .block(new Builder()
                                .beginDeclarations()
                                .variable('p', Constant.of(1))
                                .endDeclarations()
                                .beginInstructions()
                                .assign('k', Addition.of(Variable.named('k'), Constant.of(2)))
                                .forLoop('i', Subtraction.of(Variable.named('k'), Constant.of(2)), new InstructionBuilder()
                                        .assign('i', Addition.of(Variable.named('i'), Constant.of(2)))
                                        .ifStatement(Modulo.of(Variable.named('k'), Variable.named('i')), IfStatement.Type.Equal, Constant.of(0), new InstructionBuilder()
                                                .assign('p', Constant.of(0))
                                                .build()
                                        )
                                        .build()
                                )
                                .ifStatement(Variable.named('p'), IfStatement.Type.Equal, Constant.of(1), new InstructionBuilder()
                                        .print(Variable.named('k'))
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
