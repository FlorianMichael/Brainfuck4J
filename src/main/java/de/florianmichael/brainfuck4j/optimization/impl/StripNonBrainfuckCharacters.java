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

import de.florianmichael.brainfuck4j.optimization.AOptimization;
import de.florianmichael.brainfuck4j.util.Logger;

import static de.florianmichael.brainfuck4j.BFConstants.*;

public class StripNonBrainfuckCharacters extends AOptimization {

    public StripNonBrainfuckCharacters(Logger logger) {
        super(logger);
    }

    @Override
    public String fix(String input) {
        final StringBuilder output = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == increase_memory_pointer || c == decrease_memory_pointer || c == increase_value || c == decrease_value || c == start_while_loop || c == if_condition_and_jump_back || c == get_char || c == put_char) {
                output.append(c);
            }
        }
        return output.toString();
    }
}
