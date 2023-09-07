/*
 * Copyright 2021 Delft University of Technology
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
package dev.c0ps.maven;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import dev.c0ps.maven.data.Dependency;
import dev.c0ps.maven.data.Exclusion;
import dev.c0ps.maven.data.Pom;
import dev.c0ps.maven.data.Scope;
import dev.c0ps.maven.data.VersionConstraint;

public class MavenUtilitiesTest {

    @Test
    public void simplifyPom() {
        var deps = new LinkedHashSet<>(Set.of(new Dependency("dg", "da", "0.3.5")));
        var dms = new LinkedHashSet<>(Set.of(new Dependency("dmg", "dma", "1.4.6")));
        var p = new Pom("g", "a", "jar", "0.0.1", "a:b:1.2.3", 12345678901234L, "pn", deps, dms, "https://...", "atag", "url", "repo");

        var actual = MavenUtilities.simplify(p);
        var expected = new Pom("g", "a", null, "0.0.1", null, 12345678901234L, null, deps, dms, null, null, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void simplifyDep() {
        Set<VersionConstraint> vcs = Set.of();
        Set<Exclusion> exs = Set.of();
        var d = new Dependency("g", "a", vcs, exs, Scope.IMPORT, false, "t", "c");
        var p = new Pom(null, null, null, null, null, 0, null, new LinkedHashSet<>(Set.of(d)), null, null, null, null, null);
        var actual = MavenUtilities.simplify(p).dependencies.iterator().next();
        var expected = new Dependency("g", "a", vcs, exs, Scope.IMPORT, false, null, null);
        assertEquals(expected, actual);
    }
}