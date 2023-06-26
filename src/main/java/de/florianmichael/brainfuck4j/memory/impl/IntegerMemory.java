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

import de.florianmichael.brainfuck4j.exception.BFRuntimeException;
import de.florianmichael.brainfuck4j.memory.AMemory;

public class IntegerMemory extends AMemory {

    private final int[] memory;

    public IntegerMemory(final int size) {
        super("Integer", size);

        this.memory = new int[size];
    }

    @Override
    public void increase_memory_pointer(int value) {
        if (currentPointer < size - 1) {
            currentPointer += value;
        } else {
            throw new BFRuntimeException(BFRuntimeException.Type.MEMORY_OVERFLOW);
        }
    }

    @Override
    public void decrease_memory_pointer(int value) {
        if (currentPointer != 0) {
            currentPointer -= value;
        } else {
            throw new BFRuntimeException(BFRuntimeException.Type.MEMORY_UNDERFLOW);
        }
    }

    @Override
    public void increase_value(byte value) {
        memory[currentPointer] += value;
    }

    @Override
    public void decrease_value(byte value) {
        memory[currentPointer] -= value;
    }

    @Override
    public boolean isNull() {
        return memory[currentPointer] == 0;
    }

    @Override
    public void set(char c) {
        memory[currentPointer] = (byte) c;
    }

    @Override
    public char get() {
        return (char) memory[currentPointer];
    }

    @Override
    public AMemory next(int size) {
        return new IntegerMemory(size);
    }
}
