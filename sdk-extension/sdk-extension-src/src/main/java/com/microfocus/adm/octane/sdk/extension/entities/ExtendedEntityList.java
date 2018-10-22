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

package com.microfocus.adm.octane.sdk.extension.entities;

import com.microfocus.adm.octane.sdk.entities.EntityList;
import com.microfocus.adm.octane.sdk.network.OctaneHttpClient;

/**
 * Extension of the entity list, used to provide and {@link ExtendedGetEntities}
 */
public final class ExtendedEntityList extends EntityList {

    public ExtendedEntityList(OctaneHttpClient octaneHttpClient, String entityListDomain) {
        super(octaneHttpClient, entityListDomain);
    }

    /**
     * Overrides original get from {@link EntityList}
     * @return an extended version of the original {@link com.microfocus.adm.octane.sdk.entities.get.GetEntities}
     */
    public ExtendedGetEntities get() {
        return new ExtendedGetEntities(octaneHttpClient, urlDomain);
    }

}