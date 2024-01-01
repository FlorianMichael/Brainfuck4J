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

package de.florianmichael.brainfuck4j.language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents all operations which exist in the Brainfuck specification
 */
public class Dialect {
    public final static List<Dialect> DEFAULTS = Arrays.asList(
            new Dialect("Brainfuck", ">", "<", "+", "-", "[", "]", ",", "."),
            new Dialect("Trollscript", "ooo", "ool", "olo", "oll", "loo", "lol", "llo", "lll"),
            new Dialect("Ook!", "Ook. Ook?", "Ook? Ook.", "Ook. Ook.", "Ook! Ook!", "Ook! Ook.", "Ook. Ook!", "Ook! Ook?", "Ook? Ook!"),
            new Dialect("COW", "moO", "mOo", "MoO", "MOo", "MOO", "moo", "Moo", "Moo")
    );

    public final String name;
    public final String increase_memory_pointer;
    public final String decrease_memory_pointer;
    public final String increase_value;
    public final String decrease_value;
    public final String start_loop;
    public final String end_loop;
    public final String get_char;
    public final String put_char;

    public Dialect(String name, String increase_memory_pointer, String decrease_memory_pointer, String increase_value, String decrease_value, String start_loop, String end_loop, String get_char, String put_char) {
        this.name = name;
        this.increase_memory_pointer = increase_memory_pointer;
        this.decrease_memory_pointer = decrease_memory_pointer;
        this.increase_value = increase_value;
        this.decrease_value = decrease_value;
        this.start_loop = start_loop;
        this.end_loop = end_loop;
        this.get_char = get_char;
        this.put_char = put_char;
    }

    /**
     * Converts Brainfuck code from one Dialect to another
     *
     * @param input The original code
     * @param from  The original dialect the code was written
     * @param to    The target dialect the code should transform to
     */
    public static void convert(String input, final Dialect from, final Dialect to) {
        final Map<String, String> diff = from.compare(to);

        final StringBuilder output = new StringBuilder();

        while (!input.isEmpty()) {
            int count = 1;
            for (Map.Entry<String, String> entry : diff.entrySet()) {
                if (input.startsWith(entry.getKey())) {
                    output.append(entry.getValue());
                    count = entry.getKey().length();
                    break;
                }
            }
            input = input.substring(count);
        }
        input = output.toString();
    }

    /**
     * Compares two Dialects and returns a Map with the differences
     *
     * @param other The other Dialect to compare with
     * @return A Map with the differences
     */
    public Map<String, String> compare(final Dialect other) {
        final Map<String, String> diff = new HashMap<>();
        diff.put(increase_memory_pointer, other.increase_memory_pointer);
        diff.put(decrease_memory_pointer, other.decrease_memory_pointer);
        diff.put(increase_value, other.increase_value);
        diff.put(decrease_value, other.decrease_value);
        diff.put(start_loop, other.start_loop);
        diff.put(end_loop, other.end_loop);
        diff.put(get_char, other.get_char);
        diff.put(put_char, other.put_char);

        return diff;
    }

    @Override
    public String toString() {
        return "Dialect{" +
                "name='" + name + '\'' +
                ", increase_memory_pointer='" + increase_memory_pointer + '\'' +
                ", decrease_memory_pointer='" + decrease_memory_pointer + '\'' +
                ", increase_value='" + increase_value + '\'' +
                ", decrease_value='" + decrease_value + '\'' +
                ", start_while_loop='" + start_loop + '\'' +
                ", if_condition_and_jump_back='" + end_loop + '\'' +
                ", get_char='" + get_char + '\'' +
                ", put_char='" + put_char + '\'' +
                '}';
    }
}
