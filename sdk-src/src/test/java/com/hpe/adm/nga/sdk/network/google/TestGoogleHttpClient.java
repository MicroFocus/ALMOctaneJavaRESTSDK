/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpe.adm.nga.sdk.network.google;

import com.google.api.client.http.HttpRequest;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.LongFieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.HashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(GoogleHttpClient.class)
public class TestGoogleHttpClient {

    @Test
    public void testRequestRetry() throws Exception {

        GoogleHttpClient googleHttpClientSpy = spy(new GoogleHttpClient("http://url.com"));

        doReturn(null).when(googleHttpClientSpy, "convertOctaneRequestToGoogleHttpRequest", any(OctaneHttpRequest.class));
        doReturn(true).when(googleHttpClientSpy, "authenticate", any(Authentication.class));
        Whitebox.setInternalState(googleHttpClientSpy, "lastUsedAuthentication", PowerMockito.mock(Authentication.class));

        //Create timeout exception, the same way octane does
        ErrorModel errorModel = new ErrorModel(new HashSet<>());
        errorModel.setValue(new StringFieldModel("errorCode", "VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT"));
        errorModel.setValue(new LongFieldModel("httpStatusCode", 401L));
        OctaneException octaneException = new OctaneException(errorModel);

        doThrow(octaneException)
                .when(googleHttpClientSpy, "executeRequest", Matchers.any(HttpRequest.class));

        OctaneHttpRequest request = new OctaneHttpRequest.GetOctaneHttpRequest("http://url.com");

        try {
            googleHttpClientSpy.execute(request);
        } catch (Exception ex) {
            //this is supposed to fail, eventually
        }

        /**
         * Check if the method retried the right amount of times
         */
        verifyPrivate(
                googleHttpClientSpy,
                times(GoogleHttpClient.getHttpRequestRetryCount() + 1))
                .invoke("execute", any(), anyInt());
    }
}