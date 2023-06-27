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

package de.florianmichael.brainfuck4j.optimization.impl;

import de.florianmichael.brainfuck4j.exception.BFRuntimeException;
import de.florianmichael.brainfuck4j.optimization.AOptimization;
import de.florianmichael.brainfuck4j.util.Logger;

import static de.florianmichael.brainfuck4j.BFConstants.if_condition_and_jump_back;
import static de.florianmichael.brainfuck4j.BFConstants.start_while_loop;

public class OptimizeLoops extends AOptimization {

    public short[] loopPoints;

    public OptimizeLoops(Logger logger) {
        super(logger);
    }

    @Override
    public String fix(String input) {
        final char[] brainfuckCode = input.toCharArray();

        loopPoints = new short[brainfuckCode.length];
        short start;
        short end = (short) (brainfuckCode.length - 1);
        int in = 0;

        for (char ch : brainfuckCode) {
            if (ch == start_while_loop) in++;
            if (ch == if_condition_and_jump_back) in--;

            if (in < 0) break;
        }

        if (in != 0) {
            this.logger.error(new BFRuntimeException(BFRuntimeException.Type.INVALID_LOOK_SYNTAX));
        }

        for (start = 0; start < end; start++) {
            if (brainfuckCode[start] == start_while_loop) {
                in = 0;
                for (short i = (short) (start + 1); i <= end; i++) {
                    if (brainfuckCode[i] == if_condition_and_jump_back) {
                        if (in <= 0) {
                            loopPoints[start] = i;
                            loopPoints[i] = start;
                            break;
                        } else {
                            in--;
                        }
                    } else if (brainfuckCode[i] == start_while_loop) {
                        in++;
                    }
                }
            }
        }

        return new String(brainfuckCode);
    }
}
