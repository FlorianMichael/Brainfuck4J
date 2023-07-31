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

package de.florianmichael.brainfuck4j.memory;

import de.florianmichael.brainfuck4j.language.Instruction;
import de.florianmichael.brainfuck4j.util.ExecutionTracker;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public abstract class AMemory {

    public final int size;

    public AMemory(int size) {
        this.size = size;
    }

    public int currentPointer;

    public abstract void execute(final InputStreamReader in, final PrintStream out, final List<Instruction> instructions, final short[] loopPoints, final ExecutionTracker tracker) throws Throwable;
}
