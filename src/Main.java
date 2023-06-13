import expression.*;
import syntax.*;
import syntax.exceptions.MacchiatoCompilationException;

public class Main {
    public static void main(String[] args) {
        Macchiato m = null;
        try {
            m = new Macchiato(new Block(
                    new VariableDeclaration[]{
                            new VariableDeclaration('n', new Constant(30))
                    },
                    new Instruction[]{
                            new ForLoop('k', new Subtraction(new Variable('n'), new Constant(1)), new Instruction[]{
                                    new Block(
                                            new VariableDeclaration[]{
                                                    new VariableDeclaration('p', new Constant(1))
                                            },
                                            new Instruction[]{
                                                    new Assignment('k', new Addition(new Variable('k'), new Constant(2))),
                                                    new ForLoop('i', new Subtraction(new Variable('k'), new Constant(2)), new Instruction[]{
                                                            new Assignment('i', new Addition(new Variable('i'), new Constant(2))),
                                                            new IfStatement(new Modulo(new Variable('k'), new Variable('i')), IfStatement.Type.Equal, new Constant(0), new Instruction[]{
                                                                    new Assignment('p', new Constant(0))
                                                            })
                                                    }),
                                                    new IfStatement(new Variable('p'), IfStatement.Type.Equal, new Constant(1), new Instruction[]{
                                                            new Print(new Variable('k'))
                                                    })
                                            }
                                    )
                            })
                    }
            ));
        }catch(MacchiatoCompilationException e) {
            System.out.println(e.getMessage());
        }
        assert m != null;
        m.execute();
        m.debug();
    }
}
