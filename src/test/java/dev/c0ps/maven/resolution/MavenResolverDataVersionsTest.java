/*
 * Copyright 2022 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.c0ps.maven.resolution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.c0ps.maven.data.GA;
import dev.c0ps.maven.data.PomBuilder;

public class MavenResolverDataVersionsTest {

    private MavenResolverData sut;

    @BeforeEach
    public void setup() {
        sut = new MavenResolverData();
    }

    @Test
    public void multipleGAsDoNotInterfere() {
        addAt("a", 1, t(1));
        addAt("b", 1, t(1));
        assertVersionsAt("a", t(2), 1);
    }

    @Test
    public void filterIsInclusive() {
        addAt("a", 1, t(1));
        assertVersionsAt("a", t(1), 1);
    }

    @Test
    public void filterRemovesNewerReleases() {
        addAt("a", 1, t(2));
        assertVersionsAt("a", t(1));
    }

    @Test
    public void multipleVersionsGetCollected() {
        addAt("a", 1, t(1));
        addAt("a", 2, t(2));
        addAt("b", 1, t(1));
        assertVersionsAt("a", t(3), 1, 2);
    }

    private void addAt(String id, int version, Date releaseDate) {
        var pom = new PomBuilder() //
                .groupId(id).artifactId(id) //
                .releaseDate(releaseDate.getTime()) //
                .version(String.valueOf(version)) //
                .pom();
        sut.add(pom);
    }

    private void assertVersionsAt(String id, Date at, int... versions) {
        var actuals = sut.findVersions(new GA(id, id), at.getTime());
        var expecteds = Arrays.stream(versions) //
                .mapToObj(String::valueOf) //
                .collect(Collectors.toSet());
        assertEquals(expecteds, actuals);
    }

    private static Date t(int timeIdx) {
        return new Date(timeIdx);
    }
}