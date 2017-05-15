/*
 *
 *    Copyright 2017 Hewlett-Packard Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.hpe.adm.nga.sdk.examples;

import com.hpe.adm.nga.sdk.*;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.GetEntities;
import com.hpe.adm.nga.sdk.entities.GetEntity;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Demonstrates how to manipulate entities
 * Created by brucesp on 03-Jan-17.
 */

public class EntityExample {

    /**
     * We are going to assume that we are manipulating defects
     * Replace this with any entity type
     */
    private static final String ENTITY = "defects";
    /**
     * Used as a placeholder.  The assumption is that there is a valid instance of the Octane context
     */
    private final Octane octane = null;
    private final EntityList entityList;

    public EntityExample() {
        // we are going to set the entity context to be defects.  If you want to create a new entity context - just
        // change this and a new instance will be created
        entityList = octane.entityList(ENTITY);
    }

    /**
     * Returns the entity ID 2010
     */
    public void getEntity() {
        // the context of the entity list is set to ID 2010.
        final EntityList.Entities entity = entityList.at(2010);
        // we are going to use this to GET the entity
        final GetEntity get = entity.get();
        // this actually executes the REST request and gets the entity
        final EntityModel entityModel = get.execute();

        // the entity model can now be manipulated.  There will be only one since we are getting just one entity
        // so for example we can get the name field
        final FieldModel nameField = entityModel.getValue("name");

        // and get its value.  Currently this is an Object since the SDK is not type aware
        final Object value = nameField.getValue();

        // we can also get all the fields as a set and iterate
        final Set<FieldModel> values = entityModel.getValues();
    }

    /**
     * All the dates that are returned by the SDK are in Zulu time (UTC)
     * Because the SDK uses the java 1.8 time library there is an easy way to convert the time into whatever time zone you want
     * This method show an example on how you can do that for the creation time of an entity.
     */
    public void convertServerZuluTimeToLocalTime() {
        // the context of the entity list is set to ID 2010.
        final EntityList.Entities entity = entityList.at(2010);
        // we are going to use this to GET the entity
        final GetEntity get = entity.get();
        // this actually executes the REST request and gets the entity
        final EntityModel entityModel = get.execute();

        // as an example we will take the creation time for this entity
        final FieldModel creationTimeField = entityModel.getValue("creation_time");

        // for the Date and Time the SDK uses the java.time library.
        ZonedDateTime serverZuluTimeDate = (ZonedDateTime) creationTimeField.getValue();

        // After you have the time in Zulu time (UTC) you can convert it in whatever time zone you what
        // LOCAL TIME
        ZonedDateTime convertedLocalDateTime = serverZuluTimeDate.withZoneSameInstant(ZoneId.systemDefault());

        // One hour Offset
        ZonedDateTime oneHourOffsetDateTime = serverZuluTimeDate.withZoneSameInstant(ZoneOffset.ofHours(1));

        //America/Los_Angeles
        ZonedDateTime americaDateTime = serverZuluTimeDate.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
    }

    /**
     * Returns all entities of this type (defect)
     */
    public void getAllEntities() {
        // the context is for all entities
        final GetEntities get = entityList.get();

        // we execute the get.  This returns a collection; this can be queried as with one entity
        final Collection<EntityModel> entityModels = get.execute();

        // we can also add various parameters to the get
        // limit the number of entities to 10
        get.limit(10);
        // start the offset at 11
        get.offset(11);

        // we ony want the fields "name" and "type"
        get.addFields("name", "type");

        // we want to order by ID (ascending)
        get.addOrderBy("id", true);
    }

    /**
     * Here we add queries
     */
    public void getAllEntitiesWithQuery() {
        // the context is for all entities
        final GetEntities get = entityList.get();

        // build query which is the equivalent to "id eq 2"
        final Query.QueryBuilder idQueryBuilder = Query.statement("id", QueryMethod.EqualTo, 2);
        // build the query which can then be used
        final Query query = idQueryBuilder.build();

        // add to the above query so that it will be "id eq 2; name eq "defect""
        final Query.QueryBuilder queryBuilder = idQueryBuilder.and("name", QueryMethod.EqualTo, "defect");
        // then build it
        queryBuilder.build();

        // show cross filter
    }

    /**
     * Cross filter query
     * <p>
     * GET .../defects?query="user_tags EQ {id EQ 1001;(id EQ 5000000||id EQ 7000000)}"
     */
    public void getCrossFilterQuery() {
        // the context is for all entities
        final GetEntities get = entityList.get();

        final Query.QueryBuilder statement = Query.statement("user_tags", QueryMethod.EqualTo,
                Query.statement("id", QueryMethod.EqualTo, 1001)
                        .and(Query.statement("id", QueryMethod.EqualTo, 5000000)
                                .or(Query.statement("id", QueryMethod.EqualTo, 7000000))));

        // finally build it and create the Query object
        statement.build();
    }
}
