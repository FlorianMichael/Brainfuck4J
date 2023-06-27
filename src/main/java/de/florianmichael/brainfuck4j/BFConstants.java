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

public class BFConstants {

    /**
     * These are the default commands defined by the Brainfuck specification
     */
    public final static char increase_memory_pointer = '>';
    public final static char decrease_memory_pointer = '<';
    public final static char increase_value = '+';
    public final static char decrease_value = '-';
    public final static char start_while_loop = '[';
    public final static char if_condition_and_jump_back = ']';
    public final static char get_char = '.';
    public final static char put_char = ',';

    /**
     * These constants are internally used for optimizations
     */
    public final static char optimized_count_indicator = '0';

    public final static char increase_memory_optimized = 'a';
    public final static char decrease_memory_optimized = 'b';
    public final static char increase_value_optimized = 'c';
    public final static char decrease_value_optimized = 'd';

    public final static char zero_memory_cell = 'Z';

    public static String replaceGenericIncrements(final String input) {
        return input.
                replace("[-]", "Z").
                replace("[+]", "Z")
                ;
    }
}
