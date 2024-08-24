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

package de.florianmichael.brainfuck4j.exception;

import de.florianmichael.brainfuck4j.instruction.InstructionType;
import de.florianmichael.brainfuck4j.memory.AbstractMemory;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

/**
 * Thrown when {@link de.florianmichael.brainfuck4j.memory.AbstractMemory#execute(InputStreamReader, PrintStream, List, short[])} notices
 * a memory overflow/underflow.
 * <p>
 * Note that this will only happen if the given implementation via {@link AbstractMemory#handleMemoryOverflow()},
 * {@link AbstractMemory#handleMemoryUnderflow()} still uses this exception.
 */
public class MemoryException extends RuntimeException {

    private final int pointer;
    private final InstructionType instructionType;

    public MemoryException(final int pointer, final InstructionType instructionType) {
        this.pointer = pointer;
        this.instructionType = instructionType;
    }

    public int pointer() {
        return this.pointer;
    }

    public InstructionType instructionType() {
        return this.instructionType;
    }

}
