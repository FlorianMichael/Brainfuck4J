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

package de.florianmichael.brainfuck4j.util;

public class ExecutionTracker {
    private long instructions;
    private long time;

    private boolean locked;

    public void init() {
        locked = true;
        time = System.currentTimeMillis();
    }

    public void count() {
        this.instructions++;
    }

    public void close() {
        final long end = time;

        time = System.currentTimeMillis() - end;
        locked = false;
    }

    public long getInstructions() {
        return this.instructions;
    }

    public long getTime() {
        if (locked) return -1; // Calculating state

        return time;
    }
}
