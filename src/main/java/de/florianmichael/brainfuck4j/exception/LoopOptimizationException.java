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

package de.florianmichael.brainfuck4j.exception;

import java.util.List;

/**
 * Thrown when {@link de.florianmichael.brainfuck4j.Brainfuck4J#calculateLoopPoints(List)} fails to optimize loop points,
 * usually due to broken/invalid code input.
 */
public final class LoopOptimizationException extends RuntimeException {

    public LoopOptimizationException() {
        super("Invalid Loops. Please check your Brainfuck source code");
    }

}
