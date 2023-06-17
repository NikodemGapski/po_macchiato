package syntax;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public class Debugger {
    private final Scanner input;
    private int steps;
    private int scopeHeight;
    private String dumpFilePath;
    private PrintStream dumpFile;
    private enum CommandType {
        Continue,
        Step,
        Display,
        Dump,
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
                useInfo(CommandType.Dump) + '\n' +
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
            boolean validCommand = line.length() < 2 || line.charAt(1) == ' ';

            switch(type) {
                case 'c' -> {
                    if(validCommand && Objects.equals(rest, "")) {
                        nextCommandChosen = true;
                        command = CommandType.Continue;
                    }else {
                        printInvalidCommand(CommandType.Continue);
                    }
                }
                case 's' -> {
                    try {
                        if(!validCommand) throw new Exception();
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
                        if(!validCommand) throw new Exception();
                        scopeHeight = Integer.parseInt(rest);
                        if(scopeHeight < 0) throw new Exception();
                        nextCommandChosen = true;
                        command = CommandType.Display;
                    }catch(Exception e) {
                        printInvalidCommand(CommandType.Display);
                    }
                }
                case 'm' -> {
                    try {
                        if(!validCommand) throw new Exception();
                        dumpFilePath = rest;
                        dumpFile = new PrintStream(dumpFilePath);
                        nextCommandChosen = true;
                        command = CommandType.Dump;
                    }catch(Exception e) {
                        printInvalidCommand(CommandType.Dump);
                    }
                }
                case 'e' -> {
                    if(validCommand && Objects.equals(rest, "")) {
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
    // Handle all consecutive display and dump commands.
    public void handleDisplaysAndDumps(Scope scope) {
        while(isDisplay() || isDump()) {
            if(isDisplay()) {
                String output = scope.getVisibleVariables(scopeHeight);
                System.out.println(Objects.requireNonNullElse(output, "[height] parameter invalid! It should lie between 0 and the number of opened scopes."));
            }else {
                String output =
                        "Visible procedures:\n" + scope.getVisibleProcedures()
                        + "\nVisible variables: \n" + scope.getVisibleVariables();
                dumpFile.print(output);
                dumpFile.close();
                System.out.println("Dumped the memory image to " + dumpFilePath);
            }
            registerNextCommand();
        }
    }
    // Handle the current step command if the step count has hit 0
    // and handle all following display and dump commands.
    public void handleStep(String instructionName, Scope scope) {
        while(isStep() && getSteps() == 0) {
            // display the instruction
            System.out.println(instructionName);
            // wait for the next command
            registerNextCommand();
            // handle all following display and dump queries
            handleDisplaysAndDumps(scope);
        }
    }
    // Handle the current step, following displays,
    // and move step if the next command is step.
    // Returns true if the next command is exit, false otherwise.
    public boolean moveStepAndCheckExit(String instructionName, Scope scope) {
        handleStep(instructionName, scope);
        if(isExit()) return true;
        if(isStep()) moveStep();
        return false;
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
    public boolean isDump() {
        return command == CommandType.Dump;
    }
    private String useInfo(CommandType type) {
        return switch(type) {
            case Continue -> "c (continue)";
            case Step -> "s [steps] (make the number of steps and print the current line; steps > 0)";
            case Display -> "d [height] (display all variables visible at height above the current scope; height >= 0)";
            case Dump -> "m [file] (dump the current state of the program (procedures and variables) into the specified file; file must be a proper path to file)";
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
