package syntax;

import syntax.exceptions.NullArgumentException;
import syntax.exceptions.MacchiatoRuntimeException;

import java.util.Scanner;

public class Macchiato {
    private final Block main;
    public Macchiato(Block main) throws NullArgumentException {
        if(main == null) throw new NullArgumentException();
        this.main = main;
    }
    public void execute() {
        try {
            Scope mainScope = main.execute();
            System.out.println(mainScope.getVisibleVariables());
        } catch (MacchiatoRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    public void debug() {
        Debugger debugger = new Debugger(new Scanner(System.in));
        debugger.printWelcomeInfo();
        Scope mainScope = new Scope();
        try {
            debugger.registerNextCommand();
            debugger.handleDisplays(mainScope);
            if(debugger.isExit()) return;
            else if(debugger.isContinue()) mainScope = main.execute();
            else {
                debugger.moveStep();
                mainScope = main.debug(debugger);
                if(!debugger.isExit()) System.out.println(mainScope.getVisibleVariables());
            }
        }catch(MacchiatoRuntimeException e) {
            System.out.println(e.getMessage());
        }

        // wait for exit
        while(!debugger.isExit()) {
            debugger.handleDisplays(mainScope);
            if(debugger.isContinue() || debugger.isStep()) {
                System.out.println(endString());
                debugger.registerNextCommand();
            }
        }
    }
    @Override
    public String toString() {
        return "Macchiato program";
    }
    public String endString() {
        return "Macchiato finished, take a sip!";
    }
}
