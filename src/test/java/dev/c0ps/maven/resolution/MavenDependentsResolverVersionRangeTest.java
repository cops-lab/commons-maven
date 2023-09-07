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

import static dev.c0ps.maven.data.Scope.COMPILE;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dev.c0ps.maven.data.Dependency;
import dev.c0ps.maven.data.PomBuilder;
import dev.c0ps.maven.data.VersionConstraint;

public class MavenDependentsResolverVersionRangeTest extends AbstractMavenDependentsResolverTest {

    @Test
    public void softConstraint() {
        add("dest:1");
        add("a:1", $("dest", "1"));
        assertResolution("dest:1", "a:1");
    }

    @Test
    public void hardConstraint() {
        add("dest:1");
        add("a:1", $("dest", "[1]"));
        assertResolution("dest:1", "a:1");
    }

    @Test
    public void simpleRange() {
        add("dest:1.2");
        add("a:1", $("dest", "[1,2]"));
        assertResolution("dest:1.2", "a:1");
    }

    @Test
    public void multiRange() {
        add("dest:1.2");
        add("a:1", $("dest", "[1.0]", "[1.1,1.3]"));
        assertResolution("dest:1.2", "a:1");
    }

    @Test
    public void transitiveMultiRange() {
        add("dest:1.2");
        add("a:1", $("dest", "[1.0]", "[1.1,1.3]"));
        add("b:1", $("a", "[1,2]"));
        assertResolution("dest:1.2", "a:1", "b:1");
    }

    private void add(String from, Dep... tos) {
        var pb = new PomBuilder();
        var parts = from.split(":");
        pb.groupId = parts[0];
        pb.artifactId = parts[0];
        pb.version = parts[1];

        for (var to : tos) {
            var d = new Dependency(to.coord, to.coord, to.vcs, Set.of(), COMPILE, false, "jar", "");
            pb.dependencies.add(d);
        }

        data.add(pb.pom());
    }

    private static Dep $(String coord, String... vcs) {
        var dep = new Dep();
        dep.coord = coord;
        dep.vcs = Arrays.stream(vcs) //
                .map(VersionConstraint::new) //
                .collect(Collectors.toSet());
        return dep;
    }

    private static class Dep {
        public String coord;
        public Set<VersionConstraint> vcs;
    }
}