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

public abstract class AMemory {
    public final String name;
    public final int size;

    public int currentPointer;

    public AMemory(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public abstract void increase_memory_pointer(final int value) throws Exception;
    public abstract void decrease_memory_pointer(final int value) throws Exception;

    public abstract void increase_value(final byte value);
    public abstract void decrease_value(final byte value);

    public abstract boolean isNull();

    public abstract void set(final char c);
    public abstract char get();
}
