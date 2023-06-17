import expression.*;
import syntax.*;
import syntax.builders.Builder;
import syntax.exceptions.MacchiatoCompilationException;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Macchiato m = null;
        try {
            m = new Builder()
                    .beginDeclarations()
                        .variable('x', Constant.of(101))
                        .variable('y', Constant.of(1))
                        .procedure("out", List.of('a'), new Builder()
                                .beginInstructions()
                                    .print(Addition.of(Variable.named('a'), Variable.named('x')))
                                .endInstructions()
                                .build()
                        )
                    .endDeclarations()
                    .beginInstructions()
                        .assign('x', Subtraction.of(Variable.named('x'), Variable.named('y')))
                        .invoke("out", List.of(Variable.named('x')))
                        .invoke("out", List.of(Constant.of(100)))
                        .block(new Builder()
                                .beginDeclarations()
                                    .variable('x', Constant.of(10))
                                .endDeclarations()
                                .beginInstructions()
                                    .invoke("out", List.of(Constant.of(100)))
                                .endInstructions()
                                .build()
                        )
                    .endInstructions()
                    .buildMacchiato();
        }catch(MacchiatoCompilationException e) {
            System.out.println(e.getMessage());
        }
        assert m != null;
        m.execute();
        m.debug();
    }
}
