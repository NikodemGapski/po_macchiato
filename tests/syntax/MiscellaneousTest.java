package syntax;

import expression.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import syntax.builders.Builder;
import syntax.builders.InstructionBuilder;
import syntax.exceptions.MacchiatoException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiscellaneousTest {
    private StreamInterceptor io;
    @BeforeEach
    void prepare() {
        io = new StreamInterceptor();
        io.prepare();
    }
    @Test
    void forVariableReassign() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginInstructions()
                    .forLoop('i', Constant.of(5), new InstructionBuilder()
                        .print(Variable.named('i'))
                        .assign('i', Constant.of(-1)).build()
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
    void NewtonBinomial() throws MacchiatoException {
        // Calculate n over k, print the result.
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('n', Constant.of(9))
                    .variable('k', Constant.of(6))
                    .variable('x', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .forLoop('i', Subtraction.of(Variable.named('n'), Variable.named('k')), new InstructionBuilder()
                        .assign('x', Multiplication.of(Variable.named('x'), Addition.of(Variable.named('i'), Constant.of(1)))).build()
                    )
                    .forLoop('i', Subtraction.of(Variable.named('n'), Constant.of(1)), new InstructionBuilder()
                        .assign('n', Multiplication.of(Variable.named('n'), Addition.of(Variable.named('i'), Constant.of(1)))).build()
                    )
                    .forLoop('i', Subtraction.of(Variable.named('k'), Constant.of(1)), new InstructionBuilder()
                        .assign('k', Multiplication.of(Variable.named('k'), Addition.of(Variable.named('i'), Constant.of(1)))).build()
                    )
                    .print(Division.of(Division.of(Variable.named('n'), Variable.named('k')), Variable.named('x')))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        84
                        int k: 720
                        int n: 362880
                        int x: 6
                        """
        );
    }
    @Test
    void sumOfDigits() throws MacchiatoException {
        // (int holds at most 10 digits)
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('n', Constant.of(12389081))
                    .variable('r', Constant.of(0))
                    .procedure("adddigit", List.of(), new Builder()
                        .beginInstructions()
                            .assign('r', Addition.of(Variable.named('r'), Modulo.of(Variable.named('n'), Constant.of(10))))
                            .assign('n', Division.of(Variable.named('n'), Constant.of(10)))
                        .endInstructions().build()
                    )
                .endDeclarations()
                .beginInstructions()
                    .forLoop('i', Constant.of(10), new InstructionBuilder()
                        .invoke("adddigit", List.of()).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        int n: 0
                        int r: 32
                        """
        );
    }
    @Test
    void forLoopOvershadow() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('i', Constant.of(4))
                    .variable('r', Constant.of(0))
                .endDeclarations()
                .beginInstructions()
                    .forLoop('k', Constant.of(10), new InstructionBuilder()
                        .forLoop('i', Variable.named('i'), new InstructionBuilder()
                                .assign('r', Addition.of(Variable.named('r'), Constant.of(1))).build()
                        ).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        int i: 4
                        int r: 40
                        """
        );
    }
    @Test
    void recursiveFibonacci() throws MacchiatoException {
        // Calculate and print the n-th Fibonacci number.
        // To emulate return values, we make use of dynamic scoping
        // and create 2 interchanging recursive procedures.
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('b', Constant.of(0))
                // fib_a (the return value will be in the caller's b variable)
                .procedure("fiba", List.of('n'), new Builder()
                        .beginDeclarations()
                            .variable('a', Constant.of(0))
                        .endDeclarations()
                        .beginInstructions()
                            .ifStatement(Variable.named('n'), IfStatement.Type.Equal, Constant.of(0),
                                new InstructionBuilder()
                                        .assign('b', Constant.of(0)).build(),
                                new InstructionBuilder()
                                        .ifStatement(Variable.named('n'), IfStatement.Type.Equal, Constant.of(1),
                                                new InstructionBuilder()
                                                        .assign('b', Constant.of(1)).build(),
                                                new InstructionBuilder()
                                                        .invoke("fibb", List.of(Subtraction.of(Variable.named('n'), Constant.of(1))))
                                                        .assign('b', Variable.named('a'))
                                                        .invoke("fibb", List.of(Subtraction.of(Variable.named('n'), Constant.of(2))))
                                                        .assign('b', Addition.of(Variable.named('b'), Variable.named('a'))).build()
                                        ).build()
                            )
                        .endInstructions().build()
                )
                // fib_b (the return value will be in the caller's a variable)
                .procedure("fibb", List.of('n'), new Builder()
                        .beginDeclarations()
                            .variable('b', Constant.of(0))
                        .endDeclarations()
                        .beginInstructions()
                            .ifStatement(Variable.named('n'), IfStatement.Type.Equal, Constant.of(0),
                                new InstructionBuilder()
                                        .assign('a', Constant.of(0)).build(),
                                new InstructionBuilder()
                                        .ifStatement(Variable.named('n'), IfStatement.Type.Equal, Constant.of(1),
                                                new InstructionBuilder()
                                                        .assign('a', Constant.of(1)).build(),
                                                new InstructionBuilder()
                                                        .invoke("fiba", List.of(Subtraction.of(Variable.named('n'), Constant.of(1))))
                                                        .assign('a', Variable.named('b'))
                                                        .invoke("fiba", List.of(Subtraction.of(Variable.named('n'), Constant.of(2))))
                                                        .assign('a', Addition.of(Variable.named('a'), Variable.named('b'))).build()
                                        ).build()
                        )
                        .endInstructions().build()
                )
                .endDeclarations()
                .beginInstructions()
                    .forLoop('n', Constant.of(20), new InstructionBuilder()
                        .invoke("fiba", List.of(Variable.named('n')))
                        .print(Variable.named('b')).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        0
                        1
                        1
                        2
                        3
                        5
                        8
                        13
                        21
                        34
                        55
                        89
                        144
                        233
                        377
                        610
                        987
                        1597
                        2584
                        4181
                        int b: 4181
                        """
        );
    }
    @Test
    void macchiatoOldMain() throws MacchiatoException {
        Macchiato program = new Builder()
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
                                                .assign('p', Constant.of(0)).build()
                                        ).build()
                                    )
                                    .ifStatement(Variable.named('p'), IfStatement.Type.Equal, Constant.of(1), new InstructionBuilder()
                                        .print(Variable.named('k')).build()
                                    )
                                .endInstructions().build()
                        ).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();

        io.test(
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
                        int n: 30
                        """
        );
    }
}