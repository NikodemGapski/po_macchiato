# Macchiato
A simple programming language executed in Java.

## Overview
Programs in Macchiato are created in Java code by using the appropriate builders and can be executed using the execute() and debug() methods on the constructed Macchiato object.

### Operations
The language consists of declarations (specified at the beginning of a block) and instructions (some of which create new blocks).

Declarations:
- variables (letters 'a'-'z'),
- procedures.

Instructions:
- assignment,
- block,
- for loop,
- if and if-else statement,
- procedure invocation,
- printing to the standard output.

### Procedures
Procedures consist of a name, list of parameters, and an instruction (which can introduce a new block). A procedure inherits scope from the caller.

### Debugger
The debugger offers the following commands:
- countinue,
- step (performs the specified number of steps),
- display (displays all visible variables at the given relative scope),
- dump (dumps all visible variables and procedures to the specified file),
- exit.

## Author
Nikodem Gapski

The project was a semester-long assignment for OOP course at MIM UW.
