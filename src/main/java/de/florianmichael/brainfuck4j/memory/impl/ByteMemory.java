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

package de.florianmichael.brainfuck4j.memory.impl;

import de.florianmichael.brainfuck4j.instruction.InstructionType;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;

import java.io.InputStreamReader;
import java.io.PrintStream;

public class ByteMemory extends AbstractMemory {

    private final byte[] memory;

    public ByteMemory(int size) {
        super(size);
        this.memory = new byte[size];
    }

    @Override
    public int handleInstruction(InputStreamReader in, PrintStream out, InstructionType type, int count, int index, short[] loopPoints) throws Throwable {
        if (type == InstructionType.INCREASE_VALUE) {
            memory[currentPointer] += (byte) count;
        } else if (type == InstructionType.DECREASE_VALUE) {
            memory[currentPointer] -= (byte) count;
        } else if (type == InstructionType.START_LOOP) {
            if (memory[currentPointer] == 0) return loopPoints[index];
        } else if (type == InstructionType.END_LOOP) {
            if (memory[currentPointer] != 0) return loopPoints[index];
        } else if (type == InstructionType.GET_CHAR) {
            out.write(memory[currentPointer]);
        } else if (type == InstructionType.PUT_CHAR) {
            memory[currentPointer] = (byte) in.read();
        } else if (type == InstructionType.CLEAR_LOOP) {
            memory[currentPointer] = (byte) 0;
        }
        return index;
    }

}
