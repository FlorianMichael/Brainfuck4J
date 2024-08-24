# Brainfuck4J
Fast Java interpreter for Brainfuck language with optimizations, memory management and multi dialect support

## What is Brainfuck?
Brainfuck is an esoteric programming language created in 1993 by Urban Müller. <br>
Notable for its extreme minimalism, the language consists of only eight simple commands, a data pointer and an instruction pointer. While it is fully Turing complete, it is not intended for practical use, but to challenge and amuse programmers. Brainfuck requires one to break commands into microscopic steps.

Learn more about Brainfuck [here](https://en.wikipedia.org/wiki/Brainfuck)

## Contact
If you encounter any issues, please report them on the [issue tracker](https://github.com/FlorianMichael/Brainfuck4J/issues).  
If you just want to talk or need help with Brainfuck4J feel free to join my [Discord](https://discord.gg/BwWhCHUKDf).

## How to add this to your project
### Gradle/Maven
To use Brainfuck4J with Gradle/Maven you can use [Maven Central](https://mvnrepository.com/artifact/de.florianmichael/Brainfuck4J), [Lenni0451's Maven](https://maven.lenni0451.net/#/releases/de/florianmichael/Brainfuck4J) or [Jitpack](https://jitpack.io/#FlorianMichael/Brainfuck4J).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/Brainfuck4J/actions) or use the [Release](https://github.com/FlorianMichael/Brainfuck4J/releases).

## Example usage
### Dialect
For accessing multiple dialects, the ``Dialect`` class file can be used to create/save new dialects and convert between
them:
```java
// Will generate a troll script source file based on a brainfuck code
final String ts = DialectType.BRAINFUCK.convert(">++++++[<++++++>-]<.", DialectType.TROLLSCRIPT);
```
It's also possible to create a new Dialect by defining the specific operation mappings when creating a new object.
```java
final Dialect bf = new Dialect(">", "<", "+", "-", "[", "]", ",", ".");
```

### Execute a basic brainfuck code
```java
final Brainfuck4J test = new Brainfuck4J(() -> {
    // This runnable will be called as soon as the interpreter finished executing the code.
    System.out.println("Code was executed!");
});
 
test.run(
        System.in, // Output will be printed to this input stream
        System.out, // Input can be requested by this program through this output stream
        MemoryTypes.INTEGER.create(6000), // Defines the data type used to create the memory array and it's default capacity
        ">++++++[<++++++>-]<." // The brainfuck source code, this doesn't support dialects, use Dialect.convert() before calling this
);
```

### Internals and code classes
You can use the second constructor of ```Brainfuck4J``` to override the logger used to print all steps executed by the
interpreter, a logger is an ```Logger``` instance.

Note: run() doesn't throw any exceptions, they are printed through the logger which default uses ```.printStackTrace()```

Optimizations and basic parsing of the BF source code is located in the main class file ```Brainfuck4J```

All undocumented methods and classes are parts of the internals.

## Features
- [x] Basic interpreter
- [x] Dialect converter
- [x] Instruction batching
- [x] Clear loops
- [x] Pre-calculating loop points

## Planned for the future
- [ ] Application as GUI
- [ ] Executable jar file with command line
- [ ] Unit testing

#### Optimizations (http://calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html): 
- [ ] Scan loops
- [ ] Operation offsets
- [ ] Multiplication loops
- [ ] Copy loops

## Credits and sources
This program / software was developed with the help of the following resources:
- http://calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html
- http://www.hevanet.com/cristofd/brainfuck/qdb.c
- http://www.hevanet.com/cristofd/brainfuck/
- http://www.clifford.at/bfcpu/
