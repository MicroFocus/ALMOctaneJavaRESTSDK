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
package com.hpe.adm.nga.sdk.entities;

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.entities.create.CreateEntities;
import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.ModelParser;
import com.hpe.adm.nga.sdk.network.OctaneRequest;
import com.hpe.adm.nga.sdk.network.google.GoogleHttpClient;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Cookie;
import org.mockserver.model.Header;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.powermock.api.mockito.PowerMockito.spy;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestCreateEntities {

	private final static String JSON_DATA_NAME = "data";

	private static Octane octane;

	@BeforeClass
	public static void setUpBeforeClass() {
		octane = CommonMethods.getOctaneForTest();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateEntity() {
		final String jsonCreateString = "{\"data\":[{\"parent\":{\"id\":1002,\"type\":\"feature\"},\"phase\":{\"id\":1007,\"type\":\"phase\"},\"severity\":{\"id\":1004,\"type\":\"list_node\"},\"id\":1,\"name\":\"moris2\"}],\"total_count\":1}";

		EntityList defects = octane.entityList("defects");
		CreateEntities spiedCreateEntity = PowerMockito.spy(defects.create());

		try {
			Collection<EntityModel> entityModelsIn = testGetEntityModels(jsonCreateString);

			spiedCreateEntity.entities(entityModelsIn);

			Collection<EntityModel> internalModels = (Collection<EntityModel>) Whitebox.getInternalState(spiedCreateEntity, "entityModels");
			JSONObject jsonEntity = ModelParser.getInstance().getEntitiesJSONObject(internalModels);

			Collection<EntityModel> entityModelsOut = testGetEntityModels(jsonEntity.toString());
			Assert.assertTrue(CommonUtils.isCollectionAInCollectionB(entityModelsIn, entityModelsOut));
		} catch (Exception ex) {
			fail("Failed with exception: " + ex);
		}

	}

	@Test
	public void testCustomPath(){
		EntityList defects = octane.entityList("defects");

		GetEntities get = PowerMockito.spy(defects.get());

		PowerMockito.doReturn(ModelParser.getInstance().getEntities("{data:[]}")).when(get).execute();

		OctaneRequest reqBefore = (OctaneRequest)Whitebox.getInternalState(get, "octaneRequest");
		String expectedUrl = reqBefore.getOctaneUrl().toString() + "/custom/path";

		get.addPath("custom").addPath("path").execute();

		OctaneRequest reqAfter = (OctaneRequest)Whitebox.getInternalState(get, "octaneRequest");
		Assert.assertEquals("Url's don't match", expectedUrl, reqAfter.getOctaneUrl().toString());
	}

	@Test
	public void testCustomHeader() {
		ClientAndServer clientAndServer = new ClientAndServer(666);
		Cookie firstCookie = new Cookie("LWSSO_COOKIE_KEY", "one");

		clientAndServer
				.when(request()
					.withPath("/authentication/sign_in"),
					Times.once())
				.respond(response()
					.withStatusCode(200)
					.withCookie(firstCookie));

		try {
			Authentication authentication = new SimpleUserAuthentication("", "");
			String url = "http://localhost:" + clientAndServer.getLocalPort();
			GoogleHttpClient spyGoogleHttpClient = spy(new GoogleHttpClient(url));

			Octane octane = new Octane.Builder(authentication, spyGoogleHttpClient).Server(url).workSpace(1002).sharedSpace(1001).build();
			EntityList defects = octane.entityList("defects");
			GetEntities get = PowerMockito.spy(defects.get());

			final String jsonCreateString = "{\"data\":[{\"parent\":{\"id\":1002,\"type\":\"feature\"}," +
				 	"\"phase\":{\"id\":1007,\"type\":\"phase\"},\"severity\":{\"id\":1004,\"type\":\"list_node\"},"+
					"\"id\":1,\"name\":\"list1\"}],\"total_count\":1}";

			clientAndServer
					.when(request()
						.withMethod("GET").withPath("/api/shared_spaces/1001/workspaces/1002/defects")
						.withHeaders(Header.header("testHeader","testHeaderValue")))
					.respond(response()
						.withStatusCode(200)
						.withHeader("Content-Type", "application/json")
						.withBody(jsonCreateString));

			OctaneCollection<EntityModel> result = get.execute(new APIMode() {
				@Override
				public String getHeaderValue() {
					return "testHeaderValue";
				}

				@Override
				public String getHeaderKey() {
					return "testHeader";
				}
			});

			assertEquals("list1", result.iterator().next().getValue("name").getValue());
		} catch (Exception e) {
			clientAndServer.stop();
			throw e;
		}
	}

	private Collection<EntityModel> testGetEntityModels(String jason) {
		JSONObject jasonObj = new JSONObject(jason);
		JSONArray jasoDataArr = jasonObj.getJSONArray(JSON_DATA_NAME);
		Collection<EntityModel> entityModels = new ArrayList<>();
		IntStream.range(0, jasoDataArr.length()).forEach((i) -> entityModels.add(ModelParser.getInstance().getEntityModel(jasoDataArr.getJSONObject(i))));

		return entityModels;
	}


}
