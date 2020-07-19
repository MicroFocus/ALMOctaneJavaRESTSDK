/*
 * © Copyright 2016-2020 Micro Focus or one of its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.tests.admin;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.entities.create.CreateEntities;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

public class TestSharedSpaceAdmin {
    static {
        // for local execution
        if (System.getProperty("should.set.proxy") == null) {
            System.setProperty("should.set.proxy", "true");
        }
    }

    private static String url;
    private static Authentication authentication;
    private static String sharedSpaceId;

    @BeforeClass
    public static void init() {
        HttpUtils.SetSystemKeepAlive(false);
        HttpUtils.SetSystemProxy();

        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        url = configuration.getString("sdk.url");
        authentication = AuthenticationUtils.getAuthentication();
        sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
    }

    @Test
    public void testGetSharedSpaceAdmin() {
        Octane octane = ContextUtils.getContextSharedSpace(url, authentication, null);
        final OctaneCollection<EntityModel> entityModels = octane.entityList(Octane.NO_ENTITY).get().execute();
        Assert.assertTrue(entityModels.size() > 0);
        Assert.assertEquals("shared_space", entityModels.iterator().next().getValue("type").getValue());
    }

    @Test
    public void testGetSharedSpaceUsers() {
        Octane octane = ContextUtils.getContextSharedSpace(url, authentication, sharedSpaceId);
        final OctaneCollection<EntityModel> entityModels = octane.entityList("users").get().execute();
        Assert.assertTrue(entityModels.size() > 0);
        Assert.assertEquals("sharedspace_user", entityModels.iterator().next().getValue("type").getValue());
    }

    @Test
    public void testCreateSharedSpaceUser() {
        Octane octane = ContextUtils.getContextSharedSpace(url, authentication, sharedSpaceId);

        final EntityModel userEntityModel = new EntityModel();
        userEntityModel.setValue(new StringFieldModel("email", "test@nga.com"));
        userEntityModel.setValue(new StringFieldModel("first_name", "Tester"));
        userEntityModel.setValue(new StringFieldModel("last_name", "McTestface"));
        userEntityModel.setValue(new StringFieldModel("password", "Welcome1"));

        final CreateEntities createUsers = octane.entityList("users").create();
        createUsers.entities(Collections.singleton(userEntityModel));
        final OctaneCollection<EntityModel> octaneCollection = createUsers.execute();

        Assert.assertEquals(1, octaneCollection.size());
    }
}
