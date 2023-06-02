package syntax;

import java.util.Objects;
import java.util.Scanner;

public class Debugger {
    private final Scanner input;
    private int steps;
    private int scopeHeight;
    private enum CommandType {
        Continue,
        Step,
        Display,
        Exit
    }
    private CommandType command;
    public Debugger(Scanner input) {
        this.input = input;
        steps = 0;
        scopeHeight = 0;
        command = CommandType.Step;
    }
    public void printWelcomeInfo() {
        System.out.println("Welcome to Macchiato debugger!\nAvailable commands:\n" +
                useInfo(CommandType.Continue) + '\n' +
                useInfo(CommandType.Step) + '\n' +
                useInfo(CommandType.Display) + '\n' +
                useInfo(CommandType.Exit) + '\n' +
                "Time to find bugs in your coffee! Good luck!\n");
    }
    public void registerNextCommand() {
        boolean nextCommandChosen = false;
        while(!nextCommandChosen) {
            String line = input.nextLine().strip();
            if(line.length() == 0) continue;

            char type = line.charAt(0);
            String rest = line.substring(1).strip();

            switch(type) {
                case 'c' -> {
                    if(Objects.equals(rest, "")) {
                        nextCommandChosen = true;
                        command = CommandType.Continue;
                    }else {
                        printInvalidCommand(CommandType.Continue);
                    }
                }
                case 's' -> {
                    try {
                        steps = Integer.parseInt(rest);
                        if(steps <= 0) throw new Exception();
                        nextCommandChosen = true;
                        command = CommandType.Step;
                    }catch(Exception e) {
                        printInvalidCommand(CommandType.Step);
                    }
                }
                case 'd' -> {
                    try {
                        scopeHeight = Integer.parseInt(rest);
                        if(scopeHeight < 0) throw new Exception();
                        nextCommandChosen = true;
                        command = CommandType.Display;
                    }catch(Exception e) {
                        printInvalidCommand(CommandType.Display);
                    }
                }
                case 'e' -> {
                    if(Objects.equals(rest, "")) {
                        nextCommandChosen = true;
                        command = CommandType.Exit;
                    }else {
                        printInvalidCommand(CommandType.Exit);
                    }
                }
                default -> System.out.println("Invalid command!");
            }
        }
    }
    // Handle all consecutive display commands.
    public void handleDisplays(Scope scope) {
        while(isDisplay()) {
            String output = scope.getVisibleVariables(scopeHeight);
            System.out.println(Objects.requireNonNullElse(output, "[height] parameter invalid! It should lie between 0 and the number of opened scopes."));
            registerNextCommand();
        }
    }
    // Handle the current step command if the step count has hit 0
    // and handle all following display commands.
    public void handleStep(String instructionName, Scope scope) {
        while(isStep() && getSteps() == 0) {
            // display the instruction
            System.out.println(instructionName);
            // wait for the next command
            registerNextCommand();
            // handle all following display queries
            handleDisplays(scope);
        }
    }
    public boolean isContinue() {
        return command == CommandType.Continue;
    }
    public boolean isExit() {
        return command == CommandType.Exit;
    }
    public boolean isStep() {
        return command == CommandType.Step;
    }
    public boolean isDisplay() {
        return command == CommandType.Display;
    }
    private String useInfo(CommandType type) {
        return switch(type) {
            case Continue -> "c (continue)";
            case Step -> "s [steps] (make the number of steps and print the current line; steps > 0)";
            case Display -> "d [height] (display all variables visible at height above the current scope; height >= 0)";
            case Exit -> "e (exit)";
        };
    }
    private void printInvalidCommand(CommandType type) {
        System.out.println("Invalid command!\nUse: " + useInfo(type));
    }
    public int getSteps() {
        return steps;
    }
    public void moveStep() {
        --steps;
    }
}
