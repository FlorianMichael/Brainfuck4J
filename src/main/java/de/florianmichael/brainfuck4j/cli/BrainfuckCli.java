package de.florianmichael.brainfuck4j.cli;

import com.formdev.flatlaf.FlatLightLaf;
import de.florianmichael.brainfuck4j.Brainfuck4J;
import de.florianmichael.brainfuck4j.dialect.DialectType;
import de.florianmichael.brainfuck4j.gui.BrainfuckStudioApp;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;
import de.florianmichael.brainfuck4j.memory.MemoryType;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class BrainfuckCli {

    public static void main(final String[] args) {
        if (args.length == 0) {
            FlatLightLaf.setup();
            SwingUtilities.invokeLater(() -> new BrainfuckStudioApp().setVisible(true));
            return;
        }

        final String command = args[0];
        final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            switch (command) {
                case "run" -> run(subArgs);
                case "convert" -> convert(subArgs);
                case "list" -> list(subArgs);
                case "help", "-h", "--help" -> printUsage();
                default -> {
                    System.err.println("Unknown command: " + command);
                    printUsage();
                }
            }
        } catch (final Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.out.println("Brainfuck4J CLI");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  run <file> [--dialect <name>] [--memory <type>] [--size <n>]");
        System.out.println("      Execute a Brainfuck program from file.");
        System.out.println();
        System.out.println("  convert <input> <output> --from <dialect> --to <dialect>");
        System.out.println("      Convert a program between dialects.");
        System.out.println();
        System.out.println("  list dialects");
        System.out.println("      List available dialects.");
        System.out.println("  list memories");
        System.out.println("      List available memory types.");
    }

    private static void run(final String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: run <file> [--dialect <name>] [--memory <type>] [--size <n>]");
            return;
        }

        final Path file = Path.of(args[0]);
        if (!Files.exists(file)) {
            System.err.println("File does not exist: " + file);
            return;
        }

        DialectType dialect = DialectType.BRAINFUCK;
        MemoryType memoryType = MemoryType.INTEGER;
        int memorySize = 30000;

        for (int i = 1; i < args.length; i++) {
            String opt = args[i];
            if ("--dialect".equals(opt) && i + 1 < args.length) {
                dialect = DialectType.valueOf(args[++i].toUpperCase());
            } else if ("--memory".equals(opt) && i + 1 < args.length) {
                memoryType = MemoryType.valueOf(args[++i].toUpperCase());
            } else if ("--size".equals(opt) && i + 1 < args.length) {
                memorySize = Integer.parseInt(args[++i]);
            }
        }

        String program = Files.readString(file);

        if (dialect != DialectType.BRAINFUCK) {
            program = dialect.convert(program, DialectType.BRAINFUCK);
        }

        if (memorySize <= 0) {
            memorySize = 30000;
        }

        final AbstractMemory memory = memoryType.create(memorySize);

        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        final PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        try {
            Brainfuck4J.INSTANCE.run(in, out, memory, program);

            final String output = outBuffer.toString(StandardCharsets.UTF_8);
            if (!output.isEmpty()) {
                System.out.print(output);
            }
            System.out.println();
        } catch (Throwable t) {
            System.err.println("Execution failed: " + t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    private static void convert(final String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("Usage: convert <input> <output> --from <dialect> --to <dialect>");
            return;
        }

        final Path input = Path.of(args[0]);
        final Path output = Path.of(args[1]);

        DialectType from = null;
        DialectType to = null;

        for (int i = 2; i < args.length; i++) {
            String opt = args[i];
            if ("--from".equals(opt) && i + 1 < args.length) {
                from = DialectType.valueOf(args[++i].toUpperCase());
            } else if ("--to".equals(opt) && i + 1 < args.length) {
                to = DialectType.valueOf(args[++i].toUpperCase());
            }
        }

        if (from == null || to == null) {
            System.err.println("Both --from and --to must be specified.");
            return;
        }

        if (!Files.exists(input)) {
            System.err.println("Input file does not exist: " + input);
            return;
        }

        final String source = Files.readString(input);
        final String converted = from.convert(source, to);
        Files.writeString(output, converted);
        System.out.println("Converted " + input + " -> " + output);
    }

    private static void list(final String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: list dialects|memories");
            return;
        }

        final String target = args[0].toLowerCase();
        switch (target) {
            case "dialects" -> {
                for (final DialectType type : DialectType.values()) {
                    System.out.println(type.name() + " - " + type.name);
                }
            }
            case "memories" -> {
                for (final MemoryType type : MemoryType.values()) {
                    System.out.println(type.name() + " - " + type.name);
                }
            }
            default -> System.err.println("Unknown list target: " + target);
        }
    }

}
