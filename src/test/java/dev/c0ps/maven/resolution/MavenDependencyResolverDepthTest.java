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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import dev.c0ps.maven.data.Dependency;
import dev.c0ps.maven.data.PomBuilder;

public class MavenDependencyResolverDepthTest extends AbstractMavenDependencyResolverTest {

    @Test
    public void defaultIsTransitive() {
        assertTrue(config.depth == Integer.MAX_VALUE);
    }

    @Test
    public void directDependency() {
        add(BASE, "a:1");
        assertResolution(BASE, "a:1");
    }

    @Test
    public void transitiveDeps() {
        add(BASE, "a:1");
        add("a:1", "b:1");
        add("b:1", "c:1");
        assertResolution(BASE, "a:1", "b:1", "c:1");
    }

    @Test
    public void transitiveDepsLimited() {
        config.depth = 2;
        add(BASE, "a:1");
        add(BASE, "a:1");
        add("a:1", "b:1");
        add("b:1", "c:1");
        assertResolution(BASE, "a:1", "b:1");
    }

    @Test
    public void onlyDirectDep() {
        config.depth = 1;
        add(BASE, "a:1");
        assertResolution(BASE, "a:1");
    }

    @Test
    public void onlyDirectDependencyButTransitiveExists() {
        config.depth = 1;
        add(BASE, "a:1");
        add("a:1", "b:1");
        assertResolution(BASE, "a:1");
    }

    private void add(String from, String... tos) {
        danglingGAVs.remove(from);
        var pb = new PomBuilder();
        var parts = from.split(":");
        pb.groupId = parts[0];
        pb.artifactId = parts[0];
        pb.version = parts[1];

        for (String to : tos) {
            danglingGAVs.add(to);
            var partsTo = to.split(":");
            var d = new Dependency(partsTo[0], partsTo[0], partsTo[1]);
            pb.dependencies.add(d);
        }

        data.add(pb.pom());
    }

    protected void addDangling() {
        for (var gav : new HashSet<>(danglingGAVs)) {
            add(gav);
        }
    }
}