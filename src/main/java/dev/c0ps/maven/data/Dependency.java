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
package dev.c0ps.maven.data;

import static dev.c0ps.maven.data.Scope.COMPILE;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dependency {

    private static final String JAR = "jar";
    private static final Set<Exclusion> NO_EXCLS = Set.of();
    private static final String EMPTY_STR = "";

    public final String groupId;
    public final String artifactId;
    public final boolean optional;

    private final String classifier;
    private final Scope scope;
    private final String type;

    private final VersionConstraint versionConstraint;
    private final Set<VersionConstraint> versionConstraints;
    private final Set<Exclusion> exclusions;

    private final GA ga;

    private final int hashCode;

    public Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, VersionConstraint.parseVersionSpec(version), Set.of(), Scope.COMPILE, false, JAR, "");
    }

    public Dependency(String groupId, String artifactId, Set<VersionConstraint> versionConstraints, Set<Exclusion> exclusions, Scope scope, boolean optional, String type, String classifier) {

        this.groupId = Ids.gid(groupId);
        this.artifactId = Ids.aid(artifactId);
        this.optional = optional;

        if (classifier != null && !classifier.isEmpty()) {
            this.classifier = classifier.toLowerCase();
        } else {
            this.classifier = null;
        }
        if (scope != COMPILE) {
            this.scope = scope;
        } else {
            this.scope = null;
        }
        if (type != null && !type.isEmpty() && !JAR.equals(type)) {
            this.type = type.toLowerCase();
        } else {
            this.type = null;
        }

        if (versionConstraints.size() == 1) {
            this.versionConstraint = versionConstraints.iterator().next();
            this.versionConstraints = null;
        } else {
            this.versionConstraint = null;
            this.versionConstraints = Set.copyOf(versionConstraints);
        }

        if (exclusions.isEmpty()) {
            this.exclusions = NO_EXCLS;
        } else {
            this.exclusions = Set.copyOf(exclusions);
        }

        this.ga = Ids.ga(new GA(groupId, artifactId));

        hashCode = calcHashCode();
    }

    public GA toGA() {
        return ga;
    }

    public Set<VersionConstraint> getVersionConstraints() {
        if (versionConstraints == null) {
            return Set.of(versionConstraint);
        }
        return versionConstraints;
    }

    public Set<Exclusion> getExclusions() {
        return exclusions;
    }

    public Scope getScope() {
        if (scope == null) {
            return COMPILE;
        }
        return scope;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getPackagingType() {
        if (type == null) {
            return JAR;
        }
        return type;
    }

    public String getClassifier() {
        if (classifier == null) {
            return EMPTY_STR;
        }
        return classifier;
    }

    @Deprecated
    public String[] getVersionConstraintsArr() {
        var constraints = new String[getVersionConstraints().size()];
        var i = new int[] { 0 };
        getVersionConstraints().forEach(vc -> {
            constraints[i[0]++] = vc.getSpec();
        });
        return constraints;
    }

    @Deprecated
    public String getVersion() {
        return String.join(",", this.getVersionConstraintsArr());
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int calcHashCode() {
        final var prime = 31;
        var hashCode = 0;
        hashCode = prime * hashCode + ((groupId == null) ? 0 : groupId.hashCode());
        hashCode = prime * hashCode + ((artifactId == null) ? 0 : artifactId.hashCode());
        hashCode = prime * hashCode + (optional ? 1231 : 1237);

        hashCode = prime * hashCode + ((classifier == null) ? 0 : classifier.hashCode());
        hashCode = prime * hashCode + ((scope == null) ? 0 : scope.hashCode());
        hashCode = prime * hashCode + ((type == null) ? 0 : type.hashCode());

        hashCode = prime * hashCode + ((versionConstraint == null) ? 0 : versionConstraint.hashCode());
        hashCode = prime * hashCode + ((versionConstraints == null) ? 0 : versionConstraints.hashCode());
        hashCode = prime * hashCode + ((exclusions == null) ? 0 : exclusions.hashCode());
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return hashCode == ((Dependency) obj).hashCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}