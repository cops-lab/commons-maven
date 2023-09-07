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
import java.sql.Timestamp;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * An artifact released in Maven Central.
 */
public class Revision extends MavenProduct implements Serializable {

    private static final long serialVersionUID = 3096663425693127578L;

    public DefaultArtifactVersion version;
    public Timestamp createdAt;

    public Revision() {}

    public Revision(final String groupId, final String artifactId, final String version, final Timestamp createdAt) {
        super(groupId, artifactId);

        this.version = new DefaultArtifactVersion(version);
        this.createdAt = createdAt;
    }

    public Revision(final long id, final String groupId, final String artifactId, final String version, final Timestamp createdAt) {
        super(id, groupId, artifactId);

        this.version = new DefaultArtifactVersion(version);
        this.createdAt = createdAt;
    }

    public MavenProduct product() {
        return new MavenProduct(groupId, artifactId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Revision other = (Revision) obj;
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }
}