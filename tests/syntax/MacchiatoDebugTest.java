package syntax;

import expression.*;
import org.junit.jupiter.api.BeforeEach;
import syntax.builders.Builder;
import syntax.builders.InstructionBuilder;
import syntax.exceptions.MacchiatoException;
import syntax.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import syntax.exceptions.InvalidVariableNameException;

import java.io.ByteArrayInputStream;
import java.util.List;

public class MacchiatoDebugTest {
    private static Macchiato forVariableReassign;
    private static Macchiato allInstructions;
    private StreamInterceptor io;
    @BeforeAll
    static void init() throws MacchiatoException {
        forVariableReassign = new Builder()
                .beginInstructions()
                    .forLoop('i', Constant.of(5), new InstructionBuilder()
                        .print(Variable.named('i'))
                        .assign('i', Constant.of(-1)).build()
                    )
                .endInstructions()
                .buildMacchiato();

        allInstructions = new Builder()
                .beginDeclarations()
                    .variable('a', Constant.of(2))
                    .variable('b', Constant.of(4))
                .procedure("proc", List.of('a'), new Builder()
                        .beginInstructions()
                            .forLoop('i', Variable.named('a'), new InstructionBuilder()
                                .assign('x', Addition.of(Variable.named('x'), Variable.named('i'))).build()
                            )
                        .endInstructions().build()
                )
                .endDeclarations()
                .beginInstructions()
                    .ifStatement(Variable.named('a'), IfStatement.Type.Less, Variable.named('b'), new InstructionBuilder()
                        .ifStatement(Variable.named('a'), IfStatement.Type.Equal, Variable.named('b'), new Instruction[0], new InstructionBuilder()
                                .block(new Builder()
                                        .beginDeclarations()
                                            .variable('x', Constant.of(1))
                                        .endDeclarations()
                                        .beginInstructions()
                                            .invoke("proc", List.of(Subtraction.of(Variable.named('b'), Variable.named('a'))))
                                            .print(Variable.named('x'))
                                        .endInstructions().build()
                                ).build()
                        ).build()
                )
                .endInstructions()
                .buildMacchiato();
    }
    @BeforeEach
    void prepare() {
        io = new StreamInterceptor();
        io.prepare();
    }
    @Test
    void step() {
        io.prepare("s 1\n".repeat(20) + "e\n");
        forVariableReassign.debug();
        io.test(
                """
                        Welcome to Macchiato debugger!
                        Available commands:
                        c (continue)
                        s [steps] (make the number of steps and print the current line; steps > 0)
                        d [height] (display all variables visible at height above the current scope; height >= 0)
                        m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        e (exit)
                        Time to find bugs in your coffee! Good luck!
                                                
                        begin block {
                        for(int i = 0; i < 5; ++i) {
                        print(i);
                        0
                        i = -1;
                        for(int i = 0; i < 5; ++i) {
                        print(i);
                        1
                        i = -1;
                        for(int i = 0; i < 5; ++i) {
                        print(i);
                        2
                        i = -1;
                        for(int i = 0; i < 5; ++i) {
                        print(i);
                        3
                        i = -1;
                        for(int i = 0; i < 5; ++i) {
                        print(i);
                        4
                        i = -1;
                        end for }
                        end block }
                        No variables declared.
                        Macchiato finished, take a sip!
                        Macchiato finished, take a sip!
                        """
        );
    }
    @Test
    void step2() {
        io.prepare("s 1\n".repeat(25) + "e\n");
        allInstructions.debug();
        io.test(
                """
                        Welcome to Macchiato debugger!
                        Available commands:
                        c (continue)
                        s [steps] (make the number of steps and print the current line; steps > 0)
                        d [height] (display all variables visible at height above the current scope; height >= 0)
                        m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        e (exit)
                        Time to find bugs in your coffee! Good luck!
                                                
                        begin block {
                        int a = 2;
                        int b = 4;
                        declare proc(a);
                        if(a < b) {
                        if(a == b) {
                        else {
                        begin block {
                        int x = 1;
                        call proc(b - a);
                        proc(a) {
                        begin block {
                        for(int i = 0; i < a; ++i) {
                        x = x + i;
                        for(int i = 0; i < a; ++i) {
                        x = x + i;
                        end for }
                        end block }
                        end proc }
                        print(x);
                        2
                        end block }
                        end if }
                        end if }
                        end block }
                        int a: 2
                        int b: 4
                        Macchiato finished, take a sip!
                        """
        );
    }
    @Test
    void exception() throws MacchiatoException {
        io.prepare("s 1\n".repeat(7) + "e\n");
        Macchiato program = new Builder()
                .beginDeclarations()
                    .variable('n', Constant.of(0))
                    .variable('m', Constant.of(1))
                .endDeclarations()
                .beginInstructions()
                    .print(Division.of(Variable.named('m'), Variable.named('n')))
                .endInstructions()
                .buildMacchiato();
        program.debug();
        io.test(
                """
                        Welcome to Macchiato debugger!
                        Available commands:
                        c (continue)
                        s [steps] (make the number of steps and print the current line; steps > 0)
                        d [height] (display all variables visible at height above the current scope; height >= 0)
                        m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        e (exit)
                        Time to find bugs in your coffee! Good luck!
                                                
                        begin block {
                        int n = 0;
                        int m = 1;
                        print(m / n);
                        Expression exception: Cannot divide by zero!
                        at instruction: print(m / n);
                        Visible variables:
                        int m: 1
                        int n: 0
                        Macchiato finished, take a sip!
                        Macchiato finished, take a sip!
                        Macchiato finished, take a sip!
                        """
        );
    }
    @Test
    void display() {
        io.prepare("s  3\n d 2\n d 1\n d 0\n c \n c \n c \n d 0\n d   5\nd 1\n e\n");
        forVariableReassign.debug();
        io.test(
                """
                        Welcome to Macchiato debugger!
                        Available commands:
                        c (continue)
                        s [steps] (make the number of steps and print the current line; steps > 0)
                        d [height] (display all variables visible at height above the current scope; height >= 0)
                        m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        e (exit)
                        Time to find bugs in your coffee! Good luck!
                                                
                        print(i);
                        [height] parameter invalid! It should lie between 0 and the number of opened scopes.
                        No variables declared.
                        int i: 0
                        0
                        1
                        2
                        3
                        4
                        No variables declared.
                        Macchiato finished, take a sip!
                        Macchiato finished, take a sip!
                        Macchiato finished, take a sip!
                        No variables declared.
                        [height] parameter invalid! It should lie between 0 and the number of opened scopes.
                        [height] parameter invalid! It should lie between 0 and the number of opened scopes.
                        """
        );
    }
    @Test
    void invalidCommand() {
        io.prepare("p 1 1r  f23  eqf q f23 \n          \n\n\nu\n\nma\nda\nsa\nea\nca\n   e\n");
        forVariableReassign.debug();
        io.test(
                """
                        Welcome to Macchiato debugger!
                        Available commands:
                        c (continue)
                        s [steps] (make the number of steps and print the current line; steps > 0)
                        d [height] (display all variables visible at height above the current scope; height >= 0)
                        m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        e (exit)
                        Time to find bugs in your coffee! Good luck!
                                                
                        Invalid command!
                        Invalid command!
                        Invalid command!
                        Use: m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)
                        Invalid command!
                        Use: d [height] (display all variables visible at height above the current scope; height >= 0)
                        Invalid command!
                        Use: s [steps] (make the number of steps and print the current line; steps > 0)
                        Invalid command!
                        Use: e (exit)
                        Invalid command!
                        Use: c (continue)
                        """
        );
    }
}
