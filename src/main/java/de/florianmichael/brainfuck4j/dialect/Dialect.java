/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2021-2026 FlorianMichael/EnZaXD <git@florianmichael.de> and contributors
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

import java.util.Objects;

/**
 * Model to define all keywords in the Brainfuck language specification.
 * <p>
 * For general conversion utils between dialects and a common registry;
 *
 * @see DialectUtils
 * @see DialectType
 */
public record Dialect(String increase_memory_pointer, String decrease_memory_pointer,
                      String increase_value, String decrease_value, String start_loop, String end_loop, String get_char,
                      String put_char) {

    @Override
    public String toString() {
        return "Dialect{" +
                ", increase_memory_pointer='" + increase_memory_pointer + '\'' +
                ", decrease_memory_pointer='" + decrease_memory_pointer + '\'' +
                ", increase_value='" + increase_value + '\'' +
                ", decrease_value='" + decrease_value + '\'' +
                ", start_loop='" + start_loop + '\'' +
                ", end_loop='" + end_loop + '\'' +
                ", get_char='" + get_char + '\'' +
                ", put_char='" + put_char + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dialect dialect = (Dialect) o;
        return Objects.equals(end_loop, dialect.end_loop) && Objects.equals(get_char, dialect.get_char) && Objects.equals(put_char, dialect.put_char) && Objects.equals(start_loop, dialect.start_loop) && Objects.equals(increase_value, dialect.increase_value) && Objects.equals(decrease_value, dialect.decrease_value) && Objects.equals(increase_memory_pointer, dialect.increase_memory_pointer) && Objects.equals(decrease_memory_pointer, dialect.decrease_memory_pointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(increase_memory_pointer, decrease_memory_pointer, increase_value, decrease_value, start_loop, end_loop, get_char, put_char);
    }

}
