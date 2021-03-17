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
package com.hpe.adm.nga.sdk.network;

import com.hpe.adm.nga.sdk.exception.OctaneException;
import com.hpe.adm.nga.sdk.exception.OctanePartialException;
import com.hpe.adm.nga.sdk.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * HTTP Client
 * <p>
 * Created by leufl on 2/11/2016.
 */
public abstract class OctaneHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(OctaneHttpClient.class.getName());
    private static final String ERROR_CODE_TOKEN_EXPIRED = "VALIDATION_TOKEN_EXPIRED_IDLE_TIME_OUT";
    private static final String ERROR_CODE_GLOBAL_TOKEN_EXPIRED = "VALIDATION_TOKEN_EXPIRED_GLOBAL_TIME_OUT";

    protected final Map<OctaneHttpRequest, OctaneHttpResponse> cachedRequestToResponse = new HashMap<>();
    protected final Map<OctaneHttpRequest, String> requestToEtagMap = new HashMap<>();

    //Constants
    protected static final String LWSSO_COOKIE_KEY = "LWSSO_COOKIE_KEY";
    protected static final String OCTANE_USER_COOKIE_KEY = "OCTANE_USER";

    public final OctaneHttpResponse execute(OctaneHttpRequest octaneHttpRequest) {
        try {
            return internalExecute(octaneHttpRequest);
        } catch (Octane304Exception octane304Exception) {
            //Return cached response
            return cachedRequestToResponse.get(octaneHttpRequest);
        } catch (OctaneException exception) {
            //Handle session timeout exception
            final StringFieldModel errorCodeFieldModel = (StringFieldModel) exception.getError().getValue("errorCode");
            final LongFieldModel httpStatusCode = (LongFieldModel) exception.getError().getValue(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME);

            //Handle session timeout
            if (errorCodeFieldModel != null && httpStatusCode.getValue() == 401 &&
                    (ERROR_CODE_TOKEN_EXPIRED.equals(errorCodeFieldModel.getValue())
                            || ERROR_CODE_GLOBAL_TOKEN_EXPIRED.equals(errorCodeFieldModel.getValue()))) {
                throw new OctaneAuthenticateTimeOutException();
            } else {
                throw exception;
            }
        }
    }

    protected abstract OctaneHttpResponse internalExecute(OctaneHttpRequest octaneHttpRequest);

    protected final OctaneException createUnauthorisedException() {
        final LongFieldModel statusFieldModel = new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, (long) 401);
        final ErrorModel errorModel = new ErrorModel(Collections.singleton(statusFieldModel));
        // assuming that we have a cookie and therefore can go for re-authentication...
        errorModel.setValue(new StringFieldModel("errorCode", ERROR_CODE_TOKEN_EXPIRED));
        return new OctaneException(errorModel);
    }

    protected final RuntimeException createOctaneException(final String statusMessage, final String content, final long statusCode) {
        List<String> exceptionContentList = new ArrayList<>();
        exceptionContentList.add(statusMessage);
        exceptionContentList.add(content);

        for (String exceptionContent : exceptionContentList) {
            try {
                if (ModelParser.getInstance().hasErrorModels(exceptionContent)) {
                    Collection<ErrorModel> errorModels = ModelParser.getInstance().getErrorModels(exceptionContent);
                    Collection<EntityModel> entities = ModelParser.getInstance().getEntities(exceptionContent);
                    return new OctanePartialException(errorModels, entities);
                } else if (ModelParser.getInstance().hasErrorModel(exceptionContent)) {
                    ErrorModel errorModel = ModelParser.getInstance().getErrorModelFromjson(exceptionContent);
                    errorModel.setValue(new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, statusCode));
                    return new OctaneException(errorModel);
                } else if (ModelParser.getInstance().hasServletError(exceptionContent)) {
                    ErrorModel errorModel = ModelParser.getInstance().getErrorModelFromServletJson(exceptionContent);
                    errorModel.setValue(new LongFieldModel(ErrorModel.HTTP_STATUS_CODE_PROPERTY_NAME, statusCode));
                    return new OctaneException(errorModel);
                }
            } catch (Exception ignored) {
            }
        }

        return null;
    }
}
