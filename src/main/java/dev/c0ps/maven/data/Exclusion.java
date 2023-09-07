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

import java.io.Serializable;

public class Exclusion implements Serializable {

    private static final long serialVersionUID = -1350444195222504726L;

    public final String artifactId;
    public final String groupId;
    private final int hashCode;

    public Exclusion(String groupId, String artifactId) {
        this.groupId = Ids.gid(groupId);
        this.artifactId = Ids.aid(artifactId);
        this.hashCode = calcHashCode();
    }

    public int calcHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        return result;
    }

    @Override
    public int hashCode() {
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
        return hashCode == ((Exclusion) obj).hashCode;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", groupId, artifactId);
    }

    // TODO remove everything below

    @Deprecated
    public String toJSON() {
        return String.format("%s:%s", groupId, artifactId);
    }

    @Deprecated
    public static Exclusion fromJSON(String json) {
        String[] parts = json.split(":");
        return new Exclusion(parts[0], parts[1]);
    }
}