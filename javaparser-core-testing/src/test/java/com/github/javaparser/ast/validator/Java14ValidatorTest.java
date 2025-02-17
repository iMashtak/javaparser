/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2023 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.ast.validator;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.TestUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.javaparser.ParseStart.COMPILATION_UNIT;
import static com.github.javaparser.ParserConfiguration.LanguageLevel.JAVA_14;
import static com.github.javaparser.Providers.provider;

class Java14ValidatorTest {

    private final JavaParser javaParser = new JavaParser(new ParserConfiguration().setLanguageLevel(JAVA_14));


    // TODO: Confirm FORBIDDEN - pattern matching instanceof

    // TODO: Confirm PERMITTED - text blocks permitted

    /**
     * Records are available within Java 14 (preview), Java 15 (2nd preview), and Java 16 (release).
     * The introduction of records means that they are no longer able to be used as identifiers.
     */
    @Nested
    class Record {

        @Nested
        class RecordAsIdentifierPermitted {
            @Test
            void recordUsedAsClassName() {
                String s = "public class record {}";
                ParseResult<CompilationUnit> result = javaParser.parse(COMPILATION_UNIT, provider(s));
                TestUtils.assertNoProblems(result);
            }

            @Test
            void recordUsedAsFieldName() {
                String s = "class X { int record; }";
                ParseResult<CompilationUnit> result = javaParser.parse(COMPILATION_UNIT, provider(s));
                TestUtils.assertNoProblems(result);
            }
        }

        @Nested
        class RecordDeclarationForbidden {
            @Test
            void recordDeclaration() {
                String s = "record X() { }";
                ParseResult<CompilationUnit> result = javaParser.parse(COMPILATION_UNIT, provider(s));
                TestUtils.assertProblems(result, "(line 1,col 1) Record Declarations are not supported. Pay attention that this feature is supported starting from 'JAVA_14' language level. If you need that feature the language level must be configured in the configuration before parsing the source files.");
            }
        }
    }
}
