package syntax;

import expression.Addition;
import expression.Constant;
import expression.Subtraction;
import expression.Variable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MacchiatoProceduresTest {
    @Test
    void simpleCall() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new ProcedureDeclaration(new Procedure("print", List.of('x'), new Print(new Variable('x'))))
                },
                new Instruction[]{
                        new ProcedureInvocation("print", List.of(new Constant(128)))
                }
        ));
        m.execute();
    }
    @Test
    void recursionFibonacci() throws Exception {
        // calculates and print the n-th Fibonacci number
        // to emulate return values, we make use of dynamic scoping
        // and create 2 interchanging recursive procedures
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new VariableDeclaration('b', new Constant(0)),
                        // fiba (the return value will be in the caller's b variable)
                        new ProcedureDeclaration(new Procedure("fiba", List.of('n'),
                                new Block(
                                        new Declaration[]{
                                                new VariableDeclaration('a', new Constant(0))
                                        },
                                        new Instruction[]{
                                                new IfStatement(new Variable('n'), IfStatement.Type.Equal, new Constant(0), new Instruction[]{
                                                        new Assignment('b', new Constant(0))
                                                }, new Instruction[]{ new IfStatement(new Variable('n'), IfStatement.Type.Equal, new Constant(1), new Instruction[]{
                                                        new Assignment('b', new Constant(1))
                                                }, new Instruction[]{
                                                        new ProcedureInvocation("fibb", List.of(new Subtraction(new Variable('n'), new Constant(1)))),
                                                        new Assignment('b', new Variable('a')),
                                                        new ProcedureInvocation("fibb", List.of(new Subtraction(new Variable('n'), new Constant(2)))),
                                                        new Assignment('b', new Addition(new Variable('b'), new Variable('a')))
                                                })})
                                        }
                                ))),
                        // fibb (the return value will be in the caller's a variable)
                        new ProcedureDeclaration(new Procedure("fibb", List.of('n'),
                                new Block(
                                        new Declaration[]{
                                                new VariableDeclaration('b', new Constant(0))
                                        },
                                        new Instruction[]{
                                                new IfStatement(new Variable('n'), IfStatement.Type.Equal, new Constant(0), new Instruction[]{
                                                        new Assignment('a', new Constant(0))
                                                }, new Instruction[]{ new IfStatement(new Variable('n'), IfStatement.Type.Equal, new Constant(1), new Instruction[]{
                                                        new Assignment('a', new Constant(1))
                                                }, new Instruction[]{
                                                        new ProcedureInvocation("fiba", List.of(new Subtraction(new Variable('n'), new Constant(1)))),
                                                        new Assignment('a', new Variable('b')),
                                                        new ProcedureInvocation("fiba", List.of(new Subtraction(new Variable('n'), new Constant(2)))),
                                                        new Assignment('a', new Addition(new Variable('a'), new Variable('b')))
                                                })})
                                        }
                                )))
                },
                new Instruction[]{
                        // b = fib(n)
                        new ForLoop('n', new Constant(20), new Instruction[]{
                                new ProcedureInvocation("fiba", List.of(new Variable('n'))),
                                new Print(new Variable('b'))
                        })
                }
        ));
        m.execute();
    }
    @Test
    void undefinedSymbolExceptionTest() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new ProcedureDeclaration(new Procedure("test", List.of('x'), new Print(new Addition(new Variable('x'), new Variable('y')))))
                },
                new Instruction[]{
                        new ProcedureInvocation("test", List.of(new Constant(1)))
                }
        ));
        m.execute();
    }
}
