# Brainfuck4J
One of the fastest Java implementations for the Brainfuck language

## What is Brainfuck?
Brainfuck is an esoteric programming language created in 1993 by Urban Müller. <br>
Notable for its extreme minimalism, the language consists of only eight simple commands, a data pointer and an instruction pointer. While it is fully Turing complete, it is not intended for practical use, but to challenge and amuse programmers. Brainfuck requires one to break commands into microscopic steps.

Learn more about Brainfuck [here](https://en.wikipedia.org/wiki/Brainfuck)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/FlorianMichael/Brainfuck4J/issues).  
If you just want to talk or need help with Classic4J feel free to join my
[Discord](https://discord.gg/BwWhCHUKDf).

## How to add this to your project
### Gradle/Maven
To use Brainfuck4J with Gradle/Maven you can use this [Maven server](https://maven.lenni0451.net/#/releases/de/florianmichael/Brainfuck4J) or [Jitpack](https://jitpack.io/#FlorianMichael/Brainfuck4J).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/Brainfuck4J/actions) or use the [Release](https://github.com/FlorianMichael/Brainfuck4J/releases).

This library requires you to have [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1) in your class path

## Structure
### Dialect mappings
```java
public record Dialect(
        String name, 
        String increase_memory_pointer, String decrease_memory_pointer,
        String increase_value, String decrease_value, 
        String start_while_loop, String if_condition_and_jump_back,
        String get_char, String put_char
) {}
```
### Optimization base
```java
public abstract class AOptimization {

    // Input is the code from the previous step, the method should return the optimized code from this step
    public abstract String fix(final String input) throws Exception;
}
```
To implement your own Optimization step, you can call **StepTracker#add**

### Memory Set
```java
public abstract class AMemory {

    public abstract void increase_memory_pointer(final int value) throws Exception;
    public abstract void decrease_memory_pointer(final int value) throws Exception;

    public abstract void increase_value(final byte value);
    public abstract void decrease_value(final byte value);

    public abstract boolean isNull();

    public abstract void set(final char c);
    public abstract char get();
}
```

## Example usage
### Execute a basic brainfuck code
```java
final Brainfuck4J test = new Brainfuck4J(() -> {
    System.out.println("Code was executed!");
});
 
test.run(System.in, System.out, new IntegerMemory(6000), ">++++++[<++++++>-]<.");
```
### Converting Dialects
```java
final String trollScriptSource = Dialect.convert(">++++++[<++++++>-]<.", Dialect.BRAINFUCK, Dialect.TROLLSCRIPT);
```

## Credits and sources
This program / software was developed with the help of the following resources:
- http://calmerthanyouare.org/2015/01/07/optimizing-brainfuck.html
- http://www.hevanet.com/cristofd/brainfuck/qdb.c
- http://www.hevanet.com/cristofd/brainfuck/
- http://www.clifford.at/bfcpu/
