/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2023 FlorianMichael/EnZaXD and contributors
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

package de.florianmichael.brainfuck4j.memory.impl;

import de.florianmichael.brainfuck4j.language.Instruction;
import de.florianmichael.brainfuck4j.exception.BFRuntimeException;
import de.florianmichael.brainfuck4j.language.InstructionTypes;
import de.florianmichael.brainfuck4j.memory.AMemory;
import de.florianmichael.brainfuck4j.util.ExecutionTracker;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public class IntegerMemory extends AMemory {

    private final int[] memory;

    public IntegerMemory(int size) {
        super(size);
        this.memory = new int[size];
    }

    @Override
    public void execute(InputStreamReader in, PrintStream out, List<Instruction> instructions, short[] loopPoints, ExecutionTracker tracker) throws Throwable {
        for (int i = 0; i < instructions.size(); i++) {
            final var instruction = instructions.get(i);
            if (instruction.type == InstructionTypes.INCREASE_VALUE) {
                memory[currentPointer] += instruction.count;
            } else if (instruction.type == InstructionTypes.DECREASE_VALUE) {
                memory[currentPointer] -= instruction.count;
            } else if (instruction.type == InstructionTypes.INCREASE_MEMORY_POINTER) {
                if (currentPointer < size - 1) {
                    currentPointer += instruction.count;
                } else {
                    throw new BFRuntimeException(BFRuntimeException.Type.MEMORY_OVERFLOW);
                }
            } else if (instruction.type == InstructionTypes.DECREASE_MEMORY_POINTER) {
                if (currentPointer != 0) {
                    currentPointer -= instruction.count;
                } else {
                    throw new BFRuntimeException(BFRuntimeException.Type.MEMORY_UNDERFLOW);
                }
            } else if (instruction.type == InstructionTypes.START_LOOP) {
                if (memory[currentPointer] == 0) i = loopPoints[i];
            } else if (instruction.type == InstructionTypes.END_LOOP) {
                if (memory[currentPointer] != 0) i = loopPoints[i];
            } else if (instruction.type == InstructionTypes.GET_CHAR) {
                out.write(memory[currentPointer]);
            } else if (instruction.type == InstructionTypes.PUT_CHAR) {
                memory[currentPointer] = (byte) in.read();
            } else if (instruction.type == InstructionTypes.CLEAR_LOOP) {
                memory[currentPointer] = (byte) 0;
            } else if (instruction.type == InstructionTypes.COPY_LOOP) {
                memory[currentPointer + 2] += memory[currentPointer];
                memory[currentPointer] = 0;
            }
        }
    }
}
