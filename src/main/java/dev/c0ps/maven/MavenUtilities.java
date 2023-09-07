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
package dev.c0ps.maven;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import dev.c0ps.maven.data.Dependency;
import dev.c0ps.maven.data.Pom;

public class MavenUtilities {

    public static final String MAVEN_CENTRAL_REPO = "https://repo.maven.apache.org/maven2/";

    public static Pom simplify(Pom pom) {
        var deps = pom.dependencies.stream().map(d -> simplify(d)).collect(Collectors.toCollection(LinkedHashSet::new));
        var depMgmt = pom.dependencyManagement.stream().map(d -> simplify(d)).collect(Collectors.toCollection(HashSet::new));
        return new Pom(pom.groupId, pom.artifactId, null, pom.version, null, pom.releaseDate, null, deps, depMgmt, null, null, null, null);
    }

    private static Dependency simplify(Dependency d) {
        return new Dependency(d.groupId, d.artifactId, d.getVersionConstraints(), d.getExclusions(), d.getScope(), d.optional, null, null);
    }
}