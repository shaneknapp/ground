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

package edu.berkeley.ground.api.models.postgres;

import edu.berkeley.ground.api.models.Tag;
import edu.berkeley.ground.api.models.TagFactory;
import edu.berkeley.ground.api.versions.GroundType;
import edu.berkeley.ground.db.DBClient;
import edu.berkeley.ground.db.DBClient.GroundDBConnection;
import edu.berkeley.ground.db.DbDataContainer;
import edu.berkeley.ground.db.PostgresClient.PostgresConnection;
import edu.berkeley.ground.db.QueryResults;
import edu.berkeley.ground.exceptions.EmptyResultException;
import edu.berkeley.ground.exceptions.GroundException;

import java.util.*;

public class PostgresTagFactory extends TagFactory {
  public Map<String, Tag> retrieveFromDatabaseByVersionId(GroundDBConnection connection, long id) throws GroundException {
    return this.retrieveFromDatabaseById(connection, id, "rich_version");
  }

  public Map<String, Tag> retrieveFromDatabaseByItemId(GroundDBConnection connection, long id) throws GroundException {
    return this.retrieveFromDatabaseById(connection, id, "item");
  }

  private Map<String, Tag> retrieveFromDatabaseById(GroundDBConnection connectionPointer, long id, String keyPrefix) throws GroundException {
    PostgresConnection connection = (PostgresConnection) connectionPointer;

    List<DbDataContainer> predicates = new ArrayList<>();
    predicates.add(new DbDataContainer(keyPrefix + "_id", GroundType.LONG, id));

    Map<String, Tag> result = new HashMap<>();

    QueryResults resultSet;
    try {
      resultSet = connection.equalitySelect(keyPrefix + "_tag", DBClient.SELECT_STAR, predicates);
    } catch (EmptyResultException eer) {
      return new HashMap<>();
    }

    do {
      String key = resultSet.getString(2);

      // these methods will return null if the input is null, so there's no need to check
      GroundType type = GroundType.fromString(resultSet.getString(4));

      String valueString = resultSet.getString(3);
      Object value = GroundType.stringToType(valueString, type);

      result.put(key, new Tag(id, key, value, type));
    } while (resultSet.next());

    return result;
  }

  public List<Long> getVersionIdsByTag(GroundDBConnection connection, String tag) throws GroundException {
    return this.getIdsByTag(connection, tag, "rich_version");
  }


  public List<Long> getItemIdsByTag(GroundDBConnection connection, String tag) throws GroundException {
    return this.getIdsByTag(connection, tag, "item");
  }

  private List<Long> getIdsByTag(GroundDBConnection connectionPointer, String tag, String keyPrefix) throws GroundException {
    PostgresConnection connection = (PostgresConnection) connectionPointer;

    List<DbDataContainer> predicates = new ArrayList<>();
    predicates.add(new DbDataContainer("key", GroundType.STRING, tag));

    QueryResults resultSet;
    try {
      resultSet = connection.equalitySelect(keyPrefix + "_tag", DBClient.SELECT_STAR, predicates);
    } catch (EmptyResultException eer) {
      return new ArrayList<>();
    }

    List<Long> result = new ArrayList<>();

    do {
      result.add(resultSet.getLong(1));
    } while (resultSet.next());

    return result;
  }
}
