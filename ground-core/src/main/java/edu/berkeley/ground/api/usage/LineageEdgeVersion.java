/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.ground.api.usage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.berkeley.ground.api.models.RichVersion;
import edu.berkeley.ground.api.models.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LineageEdgeVersion extends RichVersion {
  // the id of the LineageEdge containing this Version
  private long lineageEdgeId;

  // the id of the RichVersion that this LineageEdgeVersion originates from
  private long fromId;

  // the id of the RichVersion that this LineageEdgeVersion points to
  private long toId;

  @JsonCreator
  protected LineageEdgeVersion(@JsonProperty("id") long id,
                               @JsonProperty("tags") Map<String, Tag> tags,
                               @JsonProperty("structureVersionId") long structureVersionId,
                               @JsonProperty("reference") String reference,
                               @JsonProperty("referenceParameters") Map<String, String> referenceParameters,
                               @JsonProperty("fromId") long fromId,
                               @JsonProperty("toId") long toId,
                               @JsonProperty("lineageEdgeId") long lineageEdgeId) {
    super(id, tags, structureVersionId, reference, referenceParameters);

    this.lineageEdgeId = lineageEdgeId;
    this.fromId = fromId;
    this.toId = toId;
  }

  @JsonProperty
  public long getLineageEdgeId() {
    return this.lineageEdgeId;
  }

  @JsonProperty
  public long getFromId() {
    return this.fromId;
  }

  @JsonProperty
  public long getToId() {
    return this.toId;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof LineageEdgeVersion)) {
      return false;
    }

    LineageEdgeVersion otherLineageEdgeVersion = (LineageEdgeVersion) other;

    return this.lineageEdgeId == otherLineageEdgeVersion.lineageEdgeId &&
        this.fromId == otherLineageEdgeVersion.fromId &&
        this.toId == otherLineageEdgeVersion.toId &&
        this.getId() == otherLineageEdgeVersion.getId() &&
        super.equals(other);
  }
}
