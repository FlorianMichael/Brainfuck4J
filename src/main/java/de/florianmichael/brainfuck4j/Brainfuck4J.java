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
import de.florianmichael.brainfuck4j.util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Brainfuck4J extends BFConstants {
    private final StripNonBrainfuckCharacters step1;
    private final OptimizeGenericIncrements step2;
    private final OptimizeLoops step3;

    private final ExecutionTracker instructionTracker = new ExecutionTracker();

    private final Logger logger;
    private final Runnable finished;

    private long time;
    private boolean cancelled;

    public Brainfuck4J(final Logger logger, final Runnable finished) {
        this.logger = logger;
        this.finished = finished;

        this.step1 = new StripNonBrainfuckCharacters(logger);
        this.step2 = new OptimizeGenericIncrements(logger);
        this.step3 = new OptimizeLoops(logger);
    }

    public void close() {
        final long end = time;

        time = System.currentTimeMillis() - end;
        cancelled = true;

        this.finished.run();
    }

    /**
     * Apply all optimizations on the current brainfuck code, this method can be overwritten to provide/apply more steps
     */
    public String applySteps(String input) {
        this.logger.info("Applying step1 (StripNonBrainfuckCharacters) to raw code!");
        input = step1.fix(input);

        this.logger.info("Applying step2 (OptimizeGenericIncrements) to raw code!");
        input = step2.fix(input);

        this.logger.info("Applying step3 (OptimizeLoops) to raw code!");
        input = step3.fix(input);

        return input;
    }

    public void run(final InputStream input, final PrintStream output, final AMemory memory, String code) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(input);
        code = applySteps(code);

        time = System.currentTimeMillis();

        final char[] brainfuckCode = code.toCharArray();
        this.logger.info("Executing code with " + brainfuckCode.length + " operations!: " + code);
        for (int currentOperation = 0; currentOperation < brainfuckCode.length; currentOperation++) {
            final char currentCommand = brainfuckCode[currentOperation];

            if (cancelled) break;
            if (currentCommand == increase_value) {
                memory.increase_value((byte) 1);
            } else if (currentCommand == increase_value_optimized) {
                memory.increase_value((byte) (brainfuckCode[++currentOperation] - optimized_count_indicator));
            } else if (currentCommand == decrease_value) {
                memory.decrease_value((byte) 1);
            } else if (currentCommand == decrease_value_optimized) {
                memory.decrease_value((byte) (brainfuckCode[++currentOperation] - optimized_count_indicator));
            } else if (currentCommand == increase_memory_pointer) {
                memory.increase_memory_pointer(1);
            } else if (currentCommand == increase_memory_optimized) {
                memory.increase_memory_pointer(brainfuckCode[++currentOperation] - optimized_count_indicator);
            } else if (currentCommand == decrease_memory_pointer) {
                memory.decrease_memory_pointer(1);
            } else if (currentCommand == decrease_memory_optimized) {
                memory.decrease_memory_pointer(brainfuckCode[++currentOperation] - optimized_count_indicator);
            } else if (currentCommand == start_while_loop) {
                if (memory.isNull()) currentOperation = step3.loopPoints[currentOperation];
            } else if (currentCommand == if_condition_and_jump_back) {
                if (!memory.isNull()) currentOperation = step3.loopPoints[currentOperation];
            } else if (currentCommand == get_char) {
                output.print(memory.get());
            } else if (currentCommand == put_char) {
                memory.set((char) inputStreamReader.read());
            } else if (currentCommand == zero_memory_cell) {
                memory.set((char) 0);
            }
            instructionTracker.count();
        }
        close();

        this.logger.info("Executed code with: " + instructionTracker.get() + " instructions!");
        this.logger.info("Time: " + time + " (ms)");
    }

    public long getTime() {
        return time;
    }

    public ExecutionTracker getInstructionTracker() {
        return instructionTracker;
    }
}
