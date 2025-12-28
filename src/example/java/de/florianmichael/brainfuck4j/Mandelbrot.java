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

package de.florianmichael.brainfuck4j;

import de.florianmichael.brainfuck4j.memory.MemoryType;
import java.io.InputStream;

public final class Mandelbrot {

    public static void main(final String[] args) throws Throwable {
        // Mandelbrot code taken from https://github.com/erikdubbelboer/brainfuck-jit
        try (final InputStream stream = Mandelbrot.class.getResourceAsStream("/mandelbrot.bf")) {
            final String code = new String(stream.readAllBytes());

            final long timeMillis = System.currentTimeMillis();
            Brainfuck4J.INSTANCE.run(System.in, System.out, MemoryType.BYTE.create(1024), code);
            System.out.println("Execution time: " + (System.currentTimeMillis() - timeMillis) + "ms");
        }
    }

}
