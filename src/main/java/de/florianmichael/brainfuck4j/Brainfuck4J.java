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

package de.florianmichael.brainfuck4j;

import de.florianmichael.brainfuck4j.util.ExecutionTracker;
import de.florianmichael.brainfuck4j.memory.AMemory;
import de.florianmichael.brainfuck4j.optimization.impl.OptimizeGenericIncrements;
import de.florianmichael.brainfuck4j.optimization.impl.OptimizeLoops;
import de.florianmichael.brainfuck4j.optimization.impl.StripNonBrainfuckCharacters;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Brainfuck4J extends BFConstants {
    private final StripNonBrainfuckCharacters step1 = new StripNonBrainfuckCharacters();
    private final OptimizeGenericIncrements step2 = new OptimizeGenericIncrements();
    private final OptimizeLoops step3 = new OptimizeLoops();

    private final ExecutionTracker instructionTracker = new ExecutionTracker();

    private final InputStreamReader input;
    private final PrintStream output;
    private final AMemory memory;

    private long time;
    private boolean cancelled;

    public Brainfuck4J(final InputStream input, final PrintStream output, final AMemory memory) {
        this.input = new InputStreamReader(input);
        this.output = output;
        this.memory = memory;
    }

    public void close() {
        final long end = time;

        time = System.currentTimeMillis() - end;
        cancelled = true;
    }

    public void run(String input) throws IOException {
        // Apply all optimizations to the code
        System.out.println("Applying step1 (StripNonBrainfuckCharacters) to raw code!");
        input = step1.fix(input);
        System.out.println("Applying step2 (OptimizeGenericIncrements) to raw code!");
        input = step2.fix(input);
        System.out.println("Applying step3 (OptimizeLoops) to raw code!");
        input = step3.fix(input);

        time = System.currentTimeMillis();

        final char[] brainfuckCode = input.toCharArray();
        System.out.println("Executing code with " + brainfuckCode.length + " operations!: " + input);
        for (int pc = 0; pc < brainfuckCode.length; pc++) {
            char currentCommand = brainfuckCode[pc];

            if (cancelled) break;

            if (currentCommand == increase_value) {
                memory.increase_value((byte) 1);
            } else if (currentCommand == increase_value_optimized) {
                memory.increase_value((byte) (brainfuckCode[++pc] - '0'));
            } else if (currentCommand == decrease_value) {
                memory.decrease_value((byte) 1);
            } else if (currentCommand == decrease_value_optimized) {
                memory.decrease_value((byte) (brainfuckCode[++pc] - '0'));
            } else if (currentCommand == increase_memory_pointer) {
                memory.increase_memory_pointer(1);
            } else if (currentCommand == increase_memory_optimized) {
                memory.increase_memory_pointer(brainfuckCode[++pc] - '0');
            } else if (currentCommand == decrease_memory_pointer) {
                memory.decrease_memory_pointer(1);
            } else if (currentCommand == decrease_memory_optimized) {
                memory.decrease_memory_pointer(brainfuckCode[++pc] - '0');
            } else if (currentCommand == start_while_loop) {
                if (memory.isNull()) pc = step3.loopPoints[pc];
            } else if (currentCommand == if_condition_and_jump_back) {
                if (!memory.isNull()) pc = step3.loopPoints[pc];
            } else if (currentCommand == get_char) {
                output.print(memory.get());
            } else if (currentCommand == put_char) {
                memory.set((char) this.input.read());
            } else if (currentCommand == zero_memory_cell) {
                memory.set((char) 0);
            }
            instructionTracker.count();
        }
        close();
    }

    public long getTime() {
        return time;
    }

    public ExecutionTracker getInstructionTracker() {
        return instructionTracker;
    }
}
