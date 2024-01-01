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

package de.florianmichael.brainfuck4j.memory.impl;

import de.florianmichael.brainfuck4j.language.InstructionTypes;
import de.florianmichael.brainfuck4j.memory.AMemory;

import java.io.InputStreamReader;
import java.io.PrintStream;

public class ByteMemory extends AMemory {

    private final byte[] memory;

    public ByteMemory(int size) {
        super(size);
        this.memory = new byte[size];
    }

    @Override
    public int handleInstruction(InputStreamReader in, PrintStream out, InstructionTypes type, int count, int index, short[] loopPoints) throws Throwable {
        if (type == InstructionTypes.INCREASE_VALUE) {
            memory[currentPointer] += (byte) count;
        } else if (type == InstructionTypes.DECREASE_VALUE) {
            memory[currentPointer] -= (byte) count;
        } else if (type == InstructionTypes.START_LOOP) {
            if (memory[currentPointer] == 0) return loopPoints[index];
        } else if (type == InstructionTypes.END_LOOP) {
            if (memory[currentPointer] != 0) return loopPoints[index];
        } else if (type == InstructionTypes.GET_CHAR) {
            out.write(memory[currentPointer]);
        } else if (type == InstructionTypes.PUT_CHAR) {
            memory[currentPointer] = (byte) in.read();
        } else if (type == InstructionTypes.CLEAR_LOOP) {
            memory[currentPointer] = (byte) 0;
        }
        return index;
    }
}
