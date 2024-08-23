/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2021-2024 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
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

import de.florianmichael.brainfuck4j.exception.BFRuntimeException;
import de.florianmichael.brainfuck4j.language.Instruction;
import de.florianmichael.brainfuck4j.language.InstructionType;
import de.florianmichael.brainfuck4j.memory.AMemory;

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
 */
public class Brainfuck4J {

    private final Runnable closeEvent;

    /**
     * Constructs an interpreter with a default logger and no finish callback.
     */
    public Brainfuck4J() {
        this(null);
    }

    /**
     * Constructs an interpreter with a default logger and a finish callback.
     *
     * @param closeEvent A Runnable to be executed when the interpreter finishes.
     */
    public Brainfuck4J(final Runnable closeEvent) {
        this.closeEvent = closeEvent;
    }

    /**
     * Closes the interpreter by executing the finish callback if provided.
     */
    public void close() {
        if (this.closeEvent != null) {
            this.closeEvent.run();
        }
    }

    private List<Instruction> batch(final List<InstructionType> instructionTypes) {
        final List<Instruction> instructions = new ArrayList<>();
        InstructionType last = null;
        for (InstructionType type : instructionTypes) {
            if (instructions.isEmpty() || type == InstructionType.START_LOOP || type == InstructionType.END_LOOP || type == InstructionType.GET_CHAR || type == InstructionType.PUT_CHAR) {
                instructions.add(new Instruction(type));
                last = type;
                continue;
            }
            if (last == type) {
                instructions.get(instructions.size() - 1).increment();
            } else {
                instructions.add(new Instruction(type));
            }
            last = type;
        }
        return instructions;
    }

    private List<InstructionType> clearLoops(final List<InstructionType> instructions) {
        final List<InstructionType> newInstructions = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++) {
            final InstructionType old = instructions.get(i);
            if (instructions.size() - 1 > i + 2) {
                final InstructionType operator = instructions.get(i + 1);
                if (old == InstructionType.START_LOOP && (operator == InstructionType.INCREASE_VALUE || operator == InstructionType.DECREASE_VALUE) && instructions.get(i + 2) == InstructionType.END_LOOP) {
                    newInstructions.add(InstructionType.CLEAR_LOOP);
                    i += 2;
                    continue;
                }
            }
            newInstructions.add(old);
        }
        return newInstructions;
    }

    public short[] loopPoints;

    private void calculateLoopPoints(final List<Instruction> instructions) {
        loopPoints = new short[instructions.size()];
        short start;
        short end = (short) (instructions.size() - 1);
        int in = 0;

        for (Instruction instruction : instructions) {
            if (instruction.type == InstructionType.START_LOOP) in++;
            if (instruction.type == InstructionType.END_LOOP) in--;

            if (in < 0) break;
        }

        if (in != 0) {
            throw new BFRuntimeException("Invalid Loops. Please check your Brainfuck source code");
        }

        for (start = 0; start < end; start++) {
            if (instructions.get(start).type == InstructionType.START_LOOP) {
                in = 0;
                for (short i = (short) (start + 1); i <= end; i++) {
                    if (instructions.get(i).type == InstructionType.END_LOOP) {
                        if (in <= 0) {
                            loopPoints[start] = i;
                            loopPoints[i] = start;
                            break;
                        } else {
                            in--;
                        }
                    } else if (instructions.get(i).type == InstructionType.START_LOOP) {
                        in++;
                    }
                }
            }
        }
    }

    /**
     * Runs a Brainfuck program.
     *
     * @param in     An InputStream to read input from.
     * @param out    A PrintStream to write output to.
     * @param memory The memory implementation to use.
     * @param input  The Brainfuck program code as a String.
     * @return A set of all instructions from the parsed input
     * @throws Throwable If an error occurs during the execution of the code
     */
    public List<Instruction> run(final InputStream in, final PrintStream out, final AMemory memory, String input) throws Throwable {
        final List<InstructionType> instructionTypes = new ArrayList<>();

        final char[] code = input.toCharArray();
        for (char c : code) {
            final InstructionType type = InstructionType.fromLeadingCharacter(c);
            if (type == null) {
                continue;
            }
            instructionTypes.add(type);
        }

        final List<Instruction> instructions = batch(clearLoops(instructionTypes));
        calculateLoopPoints(instructions);

        final InputStreamReader inIO = new InputStreamReader(in);
        final PrintStream outIO = new PrintStream(out);

        memory.execute(inIO, outIO, instructions, loopPoints);

        this.close();
        return instructions;
    }

}
