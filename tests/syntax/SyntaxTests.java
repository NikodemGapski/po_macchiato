package syntax;

import expression.Constant;
import expression.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import syntax.builders.Builder;
import syntax.builders.InstructionBuilder;
import syntax.exceptions.*;

import java.util.List;

public class SyntaxTests {
    private StreamInterceptor io;
    @BeforeEach
    void prepare() {
        io = new StreamInterceptor();
        io.prepare();
    }
    @Test
    void print() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginInstructions()
                    .print(Constant.of(1))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        1
                        No variables declared.
                        """
        );
    }
    @Test
    void variableDeclaration() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .print(Variable.named('a'))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        1
                        int a: 1
                        """
        );
    }
    @Test
    void assign() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .assign('a', Constant.of(3))
                    .print(Variable.named('a'))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        3
                        int a: 3
                        """
        );
    }
    @Test
    void block() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .block(new Builder()
                        .beginDeclarations()
                            .variable('a', Constant.of(3))
                        .endDeclarations()
                        .beginInstructions()
                            .print(Variable.named('a'))
                        .endInstructions().build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        3
                        int a: 1
                        """
        );
    }
    @Test
    void ifStatement() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .ifStatement(Variable.named('a'), IfStatement.Type.Equal, Constant.of(2),
                            new InstructionBuilder()
                                    .print(Constant.of(-1)).build(),
                            new InstructionBuilder()
                                    .print(Constant.of(0)).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        0
                        int a: 1
                        """
        );
    }
    @Test
    void forLoop() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginInstructions()
                    .forLoop('i', Constant.of(5), new InstructionBuilder()
                        .print(Variable.named('i')).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        0
                        1
                        2
                        3
                        4
                        No variables declared.
                        """
        );
    }
    @Test
    void declareProcedure() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .procedure("proc", List.of('a', 'c', 'z'), new Print(Constant.of(1)))
                .endDeclarations()
                .buildMacchiato();
        program.execute();
    }
    @Test
    void invoke() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .procedure("proc", List.of('a', 'b'), new Builder()
                        .beginInstructions()
                            .print(Variable.named('a'))
                            .print(Variable.named('b'))
                            .print(Variable.named('c'))
                        .endInstructions().build()
                    )
                    .variable('c', Constant.of(3))
                .endDeclarations()
                .beginInstructions()
                    .invoke("proc", List.of(Constant.of(1), Variable.named('c')))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        1
                        3
                        3
                        int c: 3
                        """
        );
    }
}
