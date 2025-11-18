# Brainfuck4J
Fast Java interpreter for Brainfuck language with optimizations, memory management and multi dialect support

## What is Brainfuck?
Brainfuck is an esoteric programming language created in 1993 by Urban Müller. <br>
Notable for its extreme minimalism, the language consists of only eight simple commands, a data pointer and an instruction pointer. While it is fully Turing complete, it is not intended for practical use, but to challenge and amuse programmers. Brainfuck requires one to break commands into microscopic steps.

Learn more about Brainfuck [here](https://en.wikipedia.org/wiki/Brainfuck)

## Contact
If you encounter any issues, please report them on the [issue tracker](https://github.com/FlorianMichael/Brainfuck4J/issues).  
If you just want to talk or need help with Brainfuck4J feel free to join my [Discord](https://florianmichael.de/discord).

## Features
- Fast interpreter with multiple dialects
- Dialect converter
- Swing-based GUI
- Executable JAR with command line interface
- Optimizations (clear loops, pre-calculating loop points, instruction batching)

## Usage

### CLI & GUI

If you run the packaged JAR directly, you get both CLI and GUI entry points:

```bash
java -jar Brainfuck4J-<version>.jar            # GUI (Brainfuck Studio)
java -jar Brainfuck4J-<version>.jar run ...    # CLI
```

When using the Gradle project directly you can run:

```bash
./gradlew run                                  # GUI (no args)
./gradlew run --args="run program.bf"          # CLI
```

#### CLI commands

```text
Brainfuck4J CLI

Usage:
  run <file> [--dialect <name>] [--memory <type>] [--size <n>]
      Execute a Brainfuck program from file.

  convert <input> <output> --from <dialect> --to <dialect>
      Convert a program between dialects.

  list dialects
      List available dialects.
  list memories
      List available memory types.
```

Examples:

```bash
# Run a Brainfuck file with defaults (BRAINFUCK dialect, INTEGER memory, 30000 cells)
java -jar Brainfuck4J-<version>.jar run hello.bf

# Run with explicit dialect and memory settings
java -jar Brainfuck4J-<version>.jar run hello.bf --dialect BRAINFUCK --memory INTEGER --size 60000

# Convert between dialects
java -jar Brainfuck4J-<version>.jar convert input.bf output.troll --from BRAINFUCK --to TROLLSCRIPT

# List supported dialects and memory types
java -jar Brainfuck4J-<version>.jar list dialects
java -jar Brainfuck4J-<version>.jar list memories
```

### Library

#### Gradle/Maven
To use Brainfuck4J with Gradle/Maven you can use [Maven Central](https://mvnrepository.com/artifact/de.florianmichael/Brainfuck4J) or [Lenni0451's Maven](https://maven.lenni0451.net/#/releases/de/florianmichael/Brainfuck4J).  
You can also find instructions there on how to add it to your build script.

Gradle example (Kotlin DSL):

```kotlin
dependencies {
    implementation("de.florianmichael:Brainfuck4J:<version>")
}
```

Maven example:

```xml
<dependency>
  <groupId>de.florianmichael</groupId>
  <artifactId>Brainfuck4J</artifactId>
  <version>YOUR_VERSION_HERE</version>
</dependency>
```

#### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/Brainfuck4J/actions) or use the [Releases](https://github.com/FlorianMichael/Brainfuck4J/releases).

### Examples

#### Dialects

For accessing multiple dialects, the `DialectType` and `Dialect` classes can be used to convert between dialects or define your own:

```java
import de.florianmichael.brainfuck4j.dialect.Dialect;
import de.florianmichael.brainfuck4j.dialect.DialectType;

// Convert a Brainfuck program to TrollScript
final String trollScript = DialectType.BRAINFUCK.convert(
        ">++++++[<++++++>-]<.",
        DialectType.TROLLSCRIPT
);

// Create a custom Brainfuck-like dialect
final Dialect customDialect = new Dialect(
        ">", // increment pointer
        "<", // decrement pointer
        "+", // increment cell
        "-", // decrement cell
        "[", // loop start
        "]", // loop end
        ",", // input
        "."  // output
);
```

#### Execute Brainfuck code (library API)

The main entry point is the singleton `Brainfuck4J.INSTANCE`. You supply input/output streams, a memory implementation and the (optionally pre-converted) program:

```java
import de.florianmichael.brainfuck4j.Brainfuck4J;
import de.florianmichael.brainfuck4j.dialect.DialectType;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;
import de.florianmichael.brainfuck4j.memory.MemoryType;
import de.florianmichael.brainfuck4j.instruction.Instruction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Original program in an arbitrary dialect
final String source = ">++++++[<++++++>-]<.";
final DialectType dialect = DialectType.BRAINFUCK;

// Normalize to pure Brainfuck if needed
String program = source;
if (dialect != DialectType.BRAINFUCK) {
    program = dialect.convert(source, DialectType.BRAINFUCK);
}

// Optional input for the Brainfuck program
final byte[] inputBytes = new byte[0];
final InputStream in = new ByteArrayInputStream(inputBytes);

// Capture the output in memory (you can also pass System.out)
final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
final PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

// Choose memory type and size
final MemoryType memoryType = MemoryType.INTEGER; // or BYTE, SHORT, ...
final int memorySize = 30000;
final AbstractMemory memory = memoryType.create(memorySize);

// Run the program and get the optimized instruction list
final List<Instruction> instructions = Brainfuck4J.INSTANCE.run(in, out, memory, program);

// Read output as string
final String output = outBuffer.toString(StandardCharsets.UTF_8);
System.out.println("Program output: " + output);
System.out.println("Instructions executed: " + instructions.size());
```

#### Internals and classes

- `Brainfuck4J.INSTANCE.run(...)` performs parsing, optimizations and execution.
- `Instruction` and `InstructionType` describe the optimized instruction stream.
- `MemoryType` and `AbstractMemory` implement the memory model (byte/short/int, size, bounds checking).
- `DialectType` and `Dialect` handle multi-dialect support and conversion.

All undocumented methods and classes are considered internal implementation details and may change.

## Credits and sources
This program / software was developed with the help of the following resources:
- http://calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html
- http://www.hevanet.com/cristofd/brainfuck/qdb.c
- http://www.hevanet.com/cristofd/brainfuck/
- http://www.clifford.at/bfcpu/
