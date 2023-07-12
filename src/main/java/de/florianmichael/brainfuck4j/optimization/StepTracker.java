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

package de.florianmichael.brainfuck4j.optimization;

import de.florianmichael.brainfuck4j.optimization.impl.OptimizeGenericIncrements;
import de.florianmichael.brainfuck4j.optimization.impl.OptimizeLoops;
import de.florianmichael.brainfuck4j.optimization.impl.StripNonBrainfuckCharacters;
import de.florianmichael.brainfuck4j.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepTracker {
    private final static List<AOptimization> optimizations = new ArrayList<>();

    public final static StripNonBrainfuckCharacters StripNonBrainfuckCharacters = new StripNonBrainfuckCharacters();
    public final static OptimizeGenericIncrements OptimizeGenericIncrements = new OptimizeGenericIncrements();
    public final static OptimizeLoops OptimizeLoops = new OptimizeLoops();

    static {
        optimizations.addAll(Arrays.asList(StripNonBrainfuckCharacters, OptimizeGenericIncrements, OptimizeLoops));
    }

    public static String fix(final Logger logger, String input) throws Exception {
        for (AOptimization optimization : optimizations) {
            input = optimization.fix(input);
            logger.info("Applying Step " + optimization.getClass().getSimpleName() + " to raw code!");
        }
        return input;
    }

    public static void add(final AOptimization optimization) {
        optimizations.add(optimization);
    }
}
