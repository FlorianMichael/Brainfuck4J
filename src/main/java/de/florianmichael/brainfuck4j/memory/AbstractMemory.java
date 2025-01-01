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

package de.florianmichael.brainfuck4j.memory;

import de.florianmichael.brainfuck4j.exception.MemoryException;
import de.florianmichael.brainfuck4j.instruction.Instruction;
import de.florianmichael.brainfuck4j.instruction.InstructionType;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

/**
 * The memory. Holds the execution of common instructions with {@link #execute(InputStreamReader, PrintStream, List, short[])}.
 * <p>
 * Instructions interacting with the memory array itself are handled in sub implementations via {@link #handleInstruction(InputStreamReader, PrintStream, InstructionType, int, int, short[])}
 */
public abstract class AbstractMemory {

    public final int size;

    /**
     * @param size How many units this memory can hold before it overflows.
     */
    public AbstractMemory(int size) {
        this.size = size;
    }

    protected int currentPointer;

    public void execute(final InputStreamReader in, final PrintStream out, final List<Instruction> instructions, final short[] loopPoints) throws Throwable {
        for (int i = 0; i < instructions.size(); i++) {
            final Instruction instruction = instructions.get(i);

            if (instruction.type == InstructionType.INCREASE_MEMORY_POINTER) {
                if (currentPointer < size - 1) {
                    currentPointer += instruction.count;
                } else {
                    handleMemoryOverflow();
                }
            } else if (instruction.type == InstructionType.DECREASE_MEMORY_POINTER) {
                if (currentPointer != 0) {
                    currentPointer -= instruction.count;
                } else {
                    handleMemoryUnderflow();
                }
            } else {
                i = handleInstruction(in, out, instruction.type, instruction.count, i, loopPoints);
            }
        }
    }

    // The following methods can be overridden to provide custom behavior for memory overflow and underflow.

    protected void handleMemoryOverflow() {
        throw new MemoryException(currentPointer, InstructionType.INCREASE_MEMORY_POINTER);
    }

    protected void handleMemoryUnderflow() {
        throw new MemoryException(currentPointer, InstructionType.DECREASE_MEMORY_POINTER);
    }

    public int currentPointer() {
        return currentPointer;
    }

    public abstract int handleInstruction(final InputStreamReader in, final PrintStream out, final InstructionType type, final int count, final int index, final short[] loopPoints) throws Throwable;

}
