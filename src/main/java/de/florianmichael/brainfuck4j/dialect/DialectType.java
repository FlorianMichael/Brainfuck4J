/*
 * This file is part of Brainfuck4J - https://github.com/FlorianMichael/Brainfuck4J
 * Copyright (C) 2021-2025 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and contributors
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

/**
 * Registry of common dialects of the Brainfuck language specification.
 */
public enum DialectType {

    BRAINFUCK("Brainfuck", new Dialect(">", "<", "+", "-", "[", "]", ",", ".")),
    TROLLSCRIPT("Trollscript", new Dialect("ooo", "ool", "olo", "oll", "loo", "lol", "llo", "lll")),
    OOK("Ook!", new Dialect("Ook. Ook?", "Ook? Ook.", "Ook. Ook.", "Ook! Ook!", "Ook! Ook.", "Ook. Ook!", "Ook! Ook?", "Ook? Ook!")),
    COW("COW", new Dialect("moO", "mOo", "MoO", "MOo", "MOO", "moo", "Moo", "Moo"));

    public final String name;
    public final Dialect dialect;

    DialectType(final String name, final Dialect dialect) {
        this.name = name;
        this.dialect = dialect;
    }

    /**
     * @see DialectUtils#convert(String, Dialect, Dialect)
     */
    public String convert(final String input, final DialectType output) {
        return DialectUtils.convert(input, dialect, output.dialect);
    }

}
