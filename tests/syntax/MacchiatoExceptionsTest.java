package syntax;

import expression.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import syntax.builders.Builder;
import syntax.builders.InstructionBuilder;
import syntax.exceptions.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MacchiatoExceptionsTest {
    private StreamInterceptor io;
    @BeforeEach
    void prepare() {
        io = new StreamInterceptor();
        io.prepare();
    }
    @Test
    void repeatedDeclaration() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                    .variable('b', Constant.of(2))
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        Variable a declared twice!
                        at instruction: int a = 1;
                        Visible variables:
                        int a: 1
                        int b: 2
                        """
        );
    }
    @Test
    void undefinedSymbol() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('x', Constant.of(-1))
                .endDeclarations()
                .beginInstructions()
                    .assign('x', Constant.of(0))
                    .assign('y', Constant.of(10))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        Symbol y undefined!
                        at instruction: y = 10;
                        Visible variables:
                        int x: 0
                        """
        );
    }
    @Test
    void undefinedSymbol2() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('x', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .ifStatement(Variable.named('x'), IfStatement.Type.Equal, Variable.named('y'), new InstructionBuilder()
                        .print(Variable.named('x')).build()
                    )
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        Symbol y undefined!
                        at instruction: if(x == y) {
                        Visible variables:
                        int x: 1
                        """
        );
    }
    @Test
    void undefinedSymbol3() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                .procedure("test", List.of('x'), new Print(Addition.of(Variable.named('x'), Variable.named('y'))))
                .endDeclarations()
                .beginInstructions()
                .invoke("test", List.of(Constant.of(1)))
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        Symbol y undefined!
                        at instruction: print(x + y);
                        Visible variables:
                        int x: 1
                        """
        );
    }
    @Test
    void arithmeticException() throws MacchiatoException {
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .ifStatement(Constant.of(0), IfStatement.Type.Less, Modulo.of(Variable.named('a'), Constant.of(0)), new InstructionBuilder().build())
                .endInstructions()
                .buildMacchiato();
        program.execute();
        io.test(
                """
                        Expression exception: Cannot take modulo zero!
                        at instruction: if(0 < a % 0) {
                        Visible variables:
                        int a: 1
                        """
        );
    }
    @Test
    void invalidName() {
        assertThrows(InvalidVariableNameException.class, () -> new Builder().beginInstructions().forLoop('A', new Constant(2), new Instruction[]{}));
        try {
            new Builder().beginInstructions().forLoop('A', new Constant(2), new Instruction[]{});
        }catch(MacchiatoCompilationException e) {
            System.out.println(e.getMessage());
        }
        io.test(
                """
                        Variable names must range from 'a' to 'z'! Tried to use name: A
                        """
        );
    }
    @Test
    void invalidName2() {
        assertThrows(InvalidProcedureNameException.class, () -> new Builder().beginDeclarations().procedure("invalid_name", List.of(), new Print(Constant.of(1))));
        try {
            new Builder().beginDeclarations().procedure("invalid_name", List.of(), new Print(Constant.of(1)));
        }catch(MacchiatoCompilationException e) {
            System.out.println(e.getMessage());
        }
        io.test(
                """
                        Procedure names can only contain letters from 'a' to 'z' and be non-empty! Tried to use name: invalid_name
                        """
        );
    }
}
