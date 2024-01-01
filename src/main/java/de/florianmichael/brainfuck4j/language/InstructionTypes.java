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

public enum InstructionTypes {
    INCREASE_VALUE('+'),
    DECREASE_VALUE('-'),
    INCREASE_MEMORY_POINTER('>'),
    DECREASE_MEMORY_POINTER('<'),
    START_LOOP('['),
    END_LOOP(']'),
    GET_CHAR('.'),
    PUT_CHAR(','),
    CLEAR_LOOP(null);

    public final Character leadingCharacter;

    InstructionTypes(final Character leadingCharacter) {
        this.leadingCharacter = leadingCharacter;
    }

    public static InstructionTypes fromLeadingCharacter(final char character) {
        for (InstructionTypes value : values()) {
            if (value.leadingCharacter == null) continue;
            if (value.leadingCharacter == character) {
                return value;
            }
        }
        return null;
    }
}
