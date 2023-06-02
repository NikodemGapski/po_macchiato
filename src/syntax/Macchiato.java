package syntax;

import syntax.exceptions.NullArgumentException;
import syntax.exceptions.MacchiatoRuntimeException;
import syntax.exceptions.UndefinedVariableException;

import java.util.Scanner;

public class Macchiato implements Scope {
    private final Block main;
    public Macchiato(Block main) throws NullArgumentException {
        if(main == null) throw new NullArgumentException();
        this.main = main;
        main.setScope(this);
    }
    public void execute() {
        try {
            main.execute();
            System.out.println(main.getVisibleVariables());
        } catch (MacchiatoRuntimeException e) {
            System.out.println(e.getMessage());
        }
        main.resetRecursive();
    }
    public void debug() {
        Debugger debugger = new Debugger(new Scanner(System.in));
        debugger.printWelcomeInfo();
        try {
            debugger.registerNextCommand();
            debugger.handleDisplays(main);
            if(debugger.isExit()) return;
            else if(debugger.isContinue()) main.execute();
            else {
                debugger.moveStep();
                main.debug(debugger);
                if(!debugger.isExit()) System.out.println(main.getVisibleVariables());
            }
        }catch(MacchiatoRuntimeException e) {
            System.out.println(e.getMessage());
        }

        // wait for exit
        while(!debugger.isExit()) {
            debugger.handleDisplays(main);
            if(debugger.isContinue() || debugger.isStep()) {
                System.out.println(endString());
                debugger.registerNextCommand();
            }
        }
        main.resetRecursive();
    }
    @Override
    public void setVariable(char c, int value) throws UndefinedVariableException {
        throw new UndefinedVariableException();
    }
    @Override
    public int getVariable(char c) throws UndefinedVariableException {
        throw new UndefinedVariableException();
    }
    @Override
    public String getVisibleVariables() {
        return null;
    }
    @Override
    public String getVisibleVariables(int height) {
        return null;
    }
    @Override
    public String toString() {
        return "Macchiato program";
    }
    @Override
    public String endString() {
        return "Macchiato finished, take a sip!";
    }
}
