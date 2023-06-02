package syntax;

import expression.*;
import org.junit.jupiter.api.Test;

class MacchiatoTest {
    @Test
    void forVariableReassign() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{},
                new Instruction[]{
                        new ForLoop(
                                'i', new Constant(5),
                                new Instruction[]{
                                        new Print(new Variable('i')),
                                        new Assignment('i', new Constant(-1))
                                }
                        )
                }
        ));
        m.execute();
    }
    @Test
    void exampleBinomial() throws Exception {
        Macchiato binomial = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('n', new Constant(9)),
                        new Declaration('k', new Constant(6)),
                        new Declaration('x', new Constant(1))
                },
                new Instruction[]{
                        new ForLoop('i', new Subtraction(new Variable('n'), new Variable('k')),
                                new Instruction[]{
                                        new Assignment('x', new Multiplication(new Variable('x'), new Addition(new Variable('i'), new Constant(1))))
                                }),
                        new ForLoop('i', new Subtraction(new Variable('n'), new Constant(1)),
                                new Instruction[]{
                                        new Assignment('n', new Multiplication(new Variable('n'), new Addition(new Variable('i'), new Constant(1))))
                                }),
                        new ForLoop('i', new Subtraction(new Variable('k'), new Constant(1)),
                                new Instruction[]{
                                        new Assignment('k', new Multiplication(new Variable('k'), new Addition(new Variable('i'), new Constant(1))))
                                }),
                        new Print(new Division(
                                new Division(new Variable('n'), new Variable('k')),
                                new Variable('x'))
                        )
                }
        ));
        binomial.execute();
    }
    @Test
    void sumOfDigits() throws Exception {
        // int holds at most 10 digits
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('n', new Constant(12389081)),
                        new Declaration('r', new Constant(0))
                },
                new Instruction[]{
                        new ForLoop('i', new Constant(10),
                                new Instruction[]{
                                        new Assignment('r',
                                                new Addition(new Variable('r'),
                                                        new Modulo(new Variable('n'), new Constant(10)))),
                                        new Assignment('n',
                                                new Division(new Variable('n'), new Constant(10)))
                                })
                }
        ));
        m.execute();
    }
    @Test
    void forLoopOvershadow() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('i', new Constant(4)),
                        new Declaration('r', new Constant(0))
                },
                new Instruction[]{
                        new ForLoop('k', new Constant(10), new Instruction[]{
                                new ForLoop('i', new Variable('i'), new Instruction[]{
                                    new Assignment('r', new Addition(new Variable('r'), new Constant(1)))
                                })
                        })
                }
        ));
        m.execute();
    }
    @Test
    void ifElse() throws Exception {
        Macchiato m = new Macchiato(new Block(
                new Declaration[]{
                        new Declaration('i', new Constant(1))
                },
                new Instruction[]{
                        new IfStatement(new Variable('i'), IfStatement.Type.Equal, new Constant(2),
                                new Instruction[]{},
                                new Instruction[]{
                                        new Print(new Variable('i'))
                                })
                }
        ));
        m.execute();
    }
}