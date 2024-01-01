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

package de.florianmichael.brainfuck4j.memory;

import de.florianmichael.brainfuck4j.memory.impl.ByteMemory;
import de.florianmichael.brainfuck4j.memory.impl.IntegerMemory;
import de.florianmichael.brainfuck4j.memory.impl.ShortMemory;

import java.util.function.Function;

public enum MemoryTypes {

    BYTE("Byte", ByteMemory::new),
    SHORT("Short", ShortMemory::new),
    INTEGER("Integer", IntegerMemory::new);

    public final String name;
    private final Function<Integer, AMemory> creator;

    MemoryTypes(String name, Function<Integer, AMemory> creator) {
        this.name = name;
        this.creator = creator;
    }

    public AMemory create(final int size) {
        return creator.apply(size);
    }
}
