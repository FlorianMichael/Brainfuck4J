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

package de.florianmichael.brainfuck4j.optimization.impl;

import de.florianmichael.brainfuck4j.BFConstants;
import de.florianmichael.brainfuck4j.optimization.AOptimization;
import de.florianmichael.brainfuck4j.util.Logger;

import static de.florianmichael.brainfuck4j.BFConstants.*;

public class OptimizeGenericIncrements extends AOptimization {

    private final char[][] normalToOptimized = new char[][] {
            { increase_memory_pointer, increase_memory_optimized },
            { decrease_memory_pointer, decrease_memory_optimized },
            { increase_value, increase_value_optimized },
            { decrease_value, decrease_value_optimized }
    };

    public OptimizeGenericIncrements(Logger logger) {
        super(logger);
    }

    @Override
    public String fix(String input) {
        input = BFConstants.replaceGenericIncrements(input);
        final StringBuilder output = new StringBuilder(input);

        for (int i = 0; i < output.length(); i++) {
            final char ch = output.charAt(i);

            int position;
            int counter;
            int c;

            // Check if the instruction and be optimized
            for (c = 0; c < normalToOptimized.length; c++) {
                if (ch == normalToOptimized[c][0]) break;
            }

            // If not, continue
            if (c == normalToOptimized.length) continue;

            char normalCommand = normalToOptimized[c][0];
            char optimizedCommand = normalToOptimized[c][1];

            position = i + 1;
            counter = 1;

            while (position < output.length() && output.charAt(position) == normalCommand) {
                counter++;
                position++;
            }

            if (counter > 1) {
                output.delete(i, position);
                output.insert(i, String.valueOf(optimizedCommand) + (char) (counter + optimized_count_indicator));
                i++;
            }
        }

        return output.toString();
    }
}
