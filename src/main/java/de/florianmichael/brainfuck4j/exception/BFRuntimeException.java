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

package de.florianmichael.brainfuck4j.exception;

public class BFRuntimeException extends RuntimeException {

    public BFRuntimeException(final Type type) {
        super(type.getMessage());
    }

    public enum Type {
        MEMORY_OVERFLOW("Memory OVERFLOW when trying to increase memory pointer"),
        MEMORY_UNDERFLOW("Memory UNDERFLOW when trying to decrease memory pointer"),

        INVALID_LOOK_SYNTAX("Invalid Loops. Please check your Brainfuck source code");

        private final String message;

        Type(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
