/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2021-2025 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.florianmichael.brainfuck4j;

import de.florianmichael.brainfuck4j.exception.LoopOptimizationException;
import de.florianmichael.brainfuck4j.exception.MemoryException;
import de.florianmichael.brainfuck4j.dialect.Dialect;
import de.florianmichael.brainfuck4j.instruction.Instruction;
import de.florianmichael.brainfuck4j.instruction.InstructionType;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides an interpreter for the Brainfuck programming language.
 * Brainfuck is a minimalistic programming language that operates on a simple memory model.
 * This class allows you to run Brainfuck programs, taking input from an InputStream and
 * sending output to a PrintStream, with support for custom memory implementations.
 *
 * @see AbstractMemory
 * @see Dialect
 * @see Instruction
 */
public class Brainfuck4J {

    private final Runnable runAfter;

    /**
     * Constructs an interpreter with a default logger and no finish callback.
     */
    public Brainfuck4J() {
        this(null);
    }

    /**
     * Constructs an interpreter with a default logger and a finish callback.
     *
     * @param runAfter A Runnable to be executed when the interpreter finishes.
     */
    public Brainfuck4J(final Runnable runAfter) {
        this.runAfter = runAfter;
    }

    /**
     * Runs a Brainfuck program.
     *
     * @param in     An InputStream to read input from.
     * @param out    A PrintStream to write output to.
     * @param memory The memory implementation to use.
     * @param input  The Brainfuck program code as a String.
     * @return A set of all instructions from the parsed input
     * @throws Throwable {@link LoopOptimizationException}, {@link MemoryException} If an error occurs during the execution of the code
     */
    public List<Instruction> run(final InputStream in, final PrintStream out, final AbstractMemory memory, String input) throws Throwable {
        final List<InstructionType> initialInstructionTypes = new ArrayList<>();

        // Generate instruction type list
        final char[] code = input.toCharArray();
        for (char c : code) {
            final InstructionType type = InstructionType.byIndicator(c);
            if (type == null) {
                continue;
            }
            initialInstructionTypes.add(type);
        }

        // Run optimizations
        final List<Instruction> instructions = batch(clearLoops(initialInstructionTypes));
        calculateLoopPoints(instructions);

        final InputStreamReader inIO = new InputStreamReader(in);
        final PrintStream outIO = new PrintStream(out);

        // Execute
        memory.execute(inIO, outIO, instructions, loopPoints);

        // Finished, close the interpreter
        this.close();
        return instructions;
    }

    /**
     * Closes the interpreter by executing the finish callback if provided.
     */
    public void close() {
        if (this.runAfter != null) {
            this.runAfter.run();
        }
    }

    // ---------------------------------------------------------------------------------------
    // Optimizations

    protected List<Instruction> batch(final List<InstructionType> instructionTypes) {
        final List<Instruction> output = new ArrayList<>();

        InstructionType last = null;
        for (InstructionType type : instructionTypes) {
            if (output.isEmpty() || type == InstructionType.START_LOOP || type == InstructionType.END_LOOP || type == InstructionType.GET_CHAR || type == InstructionType.PUT_CHAR) {
                output.add(new Instruction(type));
                last = type;
                continue;
            }
            if (last == type) {
                output.get(output.size() - 1).increment();
            } else {
                output.add(new Instruction(type));
            }
            last = type;
        }
        return output;
    }

    protected List<InstructionType> clearLoops(final List<InstructionType> instructionTypes) {
        final List<InstructionType> output = new ArrayList<>();

        for (int i = 0; i < instructionTypes.size(); i++) {
            final InstructionType old = instructionTypes.get(i);
            if (instructionTypes.size() - 1 > i + 2) {
                final InstructionType operator = instructionTypes.get(i + 1);
                if (old == InstructionType.START_LOOP && (operator == InstructionType.INCREASE_VALUE || operator == InstructionType.DECREASE_VALUE) && instructionTypes.get(i + 2) == InstructionType.END_LOOP) {
                    output.add(InstructionType.CLEAR_LOOP);
                    i += 2;
                    continue;
                }
            }
            output.add(old);
        }
        return output;
    }

    protected short[] loopPoints;

    protected void calculateLoopPoints(final List<Instruction> instructionTypes) {
        loopPoints = new short[instructionTypes.size()];
        short start;
        short end = (short) (instructionTypes.size() - 1);
        int in = 0;

        for (Instruction instruction : instructionTypes) {
            if (instruction.type == InstructionType.START_LOOP) in++;
            if (instruction.type == InstructionType.END_LOOP) in--;

            if (in < 0) break;
        }

        if (in != 0) {
            throw new LoopOptimizationException();
        }

        for (start = 0; start < end; start++) {
            if (instructionTypes.get(start).type == InstructionType.START_LOOP) {
                in = 0;
                for (short i = (short) (start + 1); i <= end; i++) {
                    if (instructionTypes.get(i).type == InstructionType.END_LOOP) {
                        if (in <= 0) {
                            loopPoints[start] = i;
                            loopPoints[i] = start;
                            break;
                        } else {
                            in--;
                        }
                    } else if (instructionTypes.get(i).type == InstructionType.START_LOOP) {
                        in++;
                    }
                }
            }
        }
    }

}
