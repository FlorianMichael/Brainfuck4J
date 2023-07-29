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

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents all operations which exist in the Brainfuck specification
 */
public class Dialect {

    public static Dialect BRAINFUCK = new Dialect("Brainfuck", ">", "<", "+", "-", "[", "]", ",", ".");
    public static Dialect TROLLSCRIPT = new Dialect("Trollscript", "ooo", "ool", "olo", "oll", "loo", "lol", "llo", "lll");
    public static Dialect OOK = new Dialect("Ook!", "Ook. Ook?", "Ook? Ook.", "Ook. Ook.", "Ook! Ook!", "Ook! Ook.", "Ook. Ook!", "Ook! Ook?", "Ook? Ook!");

    public final String name;
    public final String increase_memory_pointer;
    public final String decrease_memory_pointer;
    public final String increase_value;
    public final String decrease_value;
    public final String start_while_loop;
    public final String if_condition_and_jump_back;
    public final String get_char;
    public final String put_char;

    public Dialect(String name, String increase_memory_pointer, String decrease_memory_pointer, String increase_value, String decrease_value, String start_while_loop, String if_condition_and_jump_back, String get_char, String put_char) {
        this.name = name;
        this.increase_memory_pointer = increase_memory_pointer;
        this.decrease_memory_pointer = decrease_memory_pointer;
        this.increase_value = increase_value;
        this.decrease_value = decrease_value;
        this.start_while_loop = start_while_loop;
        this.if_condition_and_jump_back = if_condition_and_jump_back;
        this.get_char = get_char;
        this.put_char = put_char;
    }

    /**
     * Converts Brainfuck code from one Dialect to another
     * @param input The original code
     * @param from The original dialect the code was written
     * @param to The target dialect the code should transform to
     * @return The transformed code
     */
    public static String convert(String input, final Dialect from, final Dialect to) {
        final Map<String, String> diff = new HashMap<>();

        diff.put(from.increase_memory_pointer, to.increase_memory_pointer);
        diff.put(from.decrease_memory_pointer, from.decrease_memory_pointer);
        diff.put(from.increase_value, to.increase_value);
        diff.put(from.decrease_value, from.decrease_value);
        diff.put(from.start_while_loop, to.start_while_loop);
        diff.put(from.if_condition_and_jump_back, from.if_condition_and_jump_back);
        diff.put(from.get_char, to.get_char);
        diff.put(from.put_char, from.put_char);

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

        return output.toString();
    }

    @Override
    public String toString() {
        return "Dialect{" +
                "name='" + name + '\'' +
                ", increase_memory_pointer='" + increase_memory_pointer + '\'' +
                ", decrease_memory_pointer='" + decrease_memory_pointer + '\'' +
                ", increase_value='" + increase_value + '\'' +
                ", decrease_value='" + decrease_value + '\'' +
                ", start_while_loop='" + start_while_loop + '\'' +
                ", if_condition_and_jump_back='" + if_condition_and_jump_back + '\'' +
                ", get_char='" + get_char + '\'' +
                ", put_char='" + put_char + '\'' +
                '}';
    }
}
