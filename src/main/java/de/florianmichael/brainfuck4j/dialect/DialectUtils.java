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

package de.florianmichael.brainfuck4j.dialect;

import java.util.HashMap;
import java.util.Map;

public final class DialectUtils {

    private DialectUtils() {
    }

    /**
     * Converts Brainfuck code from one Dialect to another
     *
     * @param input The original code
     * @param from  The original dialect the code was written
     * @param to    The target dialect the code should transform to
     * @return the converted code
     */
    public static String convert(String input, final Dialect from, final Dialect to) {
        final Map<String, String> table = mappings(from, to);
        final StringBuilder output = new StringBuilder();
        while (!input.isEmpty()) {
            int count = 1;
            for (Map.Entry<String, String> entry : table.entrySet()) {
                if (input.startsWith(entry.getKey())) {
                    output.append(entry.getValue());
                    count = entry.getKey().length();
                    break;
                }
            }
            input = input.substring(count);
        }
        return output.toString();
    }

    /**
     * Checks if both codes are equal when compared using their respective dialect.
     *
     * @param input  The first code
     * @param first  The dialect the first code was written
     * @param other  The second code
     * @param second The dialect the second code was written
     * @return true if the codes are equal, false otherwise
     */
    public static boolean equals(final String input, final Dialect first, final String other, final Dialect second) {
        return input.equals(convert(other, second, first));
    }

    private static Map<String, String> mappings(final Dialect first, final Dialect other) {
        final Map<String, String> table = new HashMap<>();
        table.put(first.increase_memory_pointer(), other.increase_memory_pointer());
        table.put(first.decrease_memory_pointer(), other.decrease_memory_pointer());
        table.put(first.increase_value(), other.increase_value());
        table.put(first.decrease_value(), other.decrease_value());
        table.put(first.start_loop(), other.start_loop());
        table.put(first.end_loop(), other.end_loop());
        table.put(first.get_char(), other.get_char());
        table.put(first.put_char(), other.put_char());
        return table;
    }

}
