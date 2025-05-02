/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.extension.businessrules;

import com.hpe.adm.nga.sdk.APIMode;
import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.entities.get.GetEntities;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.query.Query;
import com.hpe.adm.nga.sdk.query.QueryMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BusinessRulesService {

    private static final String ENTITY_NAME = "business_rules";
    private static final long MASTER_WORKSPACE_ID = 500;

    private Octane octane;
    private boolean onSharedSpace;

    private BusinessRulesService(Octane octane, boolean onMasterWorkspace) {
        this.octane = octane;
        this.onSharedSpace = onMasterWorkspace;
    }

    public Collection<BusinessRuleEntityModel> getBusinessRules() {
        GetEntities request = octane.entityList(ENTITY_NAME).get();

        if (!onSharedSpace) {
            request = request.query(Query.not("workspace_id", QueryMethod.EqualTo, Long.toString(MASTER_WORKSPACE_ID)).build());
        }

        return request.execute().stream().map(BusinessRuleEntityModel::new).collect(Collectors.toList());
    }

    public Collection<BusinessRuleEntityModel> getBusinessRulesForEntityType(String entityType) {
        GetEntities request = octane.entityList(ENTITY_NAME).get();

        if (entityType == null) {
            return getBusinessRules();
        }

        if (!onSharedSpace) {
            request = request.query(Query.not("workspace_id", QueryMethod.EqualTo, Long.toString(MASTER_WORKSPACE_ID))
                    .and(Query.statement("category", QueryMethod.EqualTo, entityType)).build());
        } else {
            request = request.query(Query.statement("category", QueryMethod.EqualTo, entityType).build());
        }

        return request.execute().stream().map(BusinessRuleEntityModel::new).collect(Collectors.toList());
    }

    public Collection<BusinessRuleEntityModel> getBusinessRulesByIds(Collection<String> businessRulesIds) {

        Query query = onSharedSpace ? Query.statement("id", QueryMethod.In, businessRulesIds.toArray()).build() :
                Query.statement("id", QueryMethod.In, businessRulesIds.toArray())
                        .and(Query.not("workspace_id", QueryMethod.EqualTo, Long.toString(MASTER_WORKSPACE_ID))).build();

        return octane.entityList(ENTITY_NAME)
                .get()
                .query(query)
                .execute()
                .stream()
                .map(BusinessRuleEntityModel::new)
                .collect(Collectors.toList());
    }

    public int clearBusinessRules() {
        return deleteBusinessRules(getBusinessRules());
    }

    public int clearBusinessRulesForEntityType(String entityType) {
        return deleteBusinessRules(getBusinessRulesForEntityType(entityType));
    }

    public Collection<BusinessRuleEntityModel> createBusinessRules(Collection<BusinessRuleEntityModel> rules) {
        rules.forEach(this::removeNonEditableFields);

        return octane.entityList(ENTITY_NAME).create()
                .entities(new ArrayList<>(rules))
                .execute()
                .stream()
                .map(BusinessRuleEntityModel::new)
                .collect(Collectors.toList());
    }

    public Collection<BusinessRuleEntityModel> updateBusinessRules(Collection<BusinessRuleEntityModel> rules) {
        rules.forEach(this::removeNonEditableFields);

        return octane.entityList(ENTITY_NAME).update()
                .entities(new ArrayList<>(rules))
                .execute()
                .stream()
                .map(BusinessRuleEntityModel::new)
                .collect(Collectors.toList());
    }

    public Collection<BusinessRuleEntityModel> deleteBusinessRulesByIds(Collection<String> businessRulesIds) {
        return octane.entityList(ENTITY_NAME)
                .delete()
                .query(Query.statement("id", QueryMethod.In, businessRulesIds.toArray()).build())
                .execute()
                .stream()
                .map(BusinessRuleEntityModel::new)
                .collect(Collectors.toList());
    }

    private void removeNonEditableFields(EntityModel rule) {
        rule.removeValue("workspace_id");
        rule.removeValue("logical_name");
        rule.removeValue("index");
        rule.removeValue("last_modified");
        rule.removeValue("version_stamp");

        rule.removeValue("shared");
        rule.removeValue("is_system");
    }

    public int deleteBusinessRules(Collection<BusinessRuleEntityModel> rules) {
        Collection<String> rulesIds = rules.stream()
                .map(rule -> ((StringFieldModel) rule.getValue("id")).getValue())
                .collect(Collectors.toList());

        if (!rulesIds.isEmpty()) {
            return deleteBusinessRulesByIds(rulesIds).size();
        } else {
            return 0;
        }
    }

    public static class Builder {

        private String username;
        private String password;
        private String url;
        private long sharedSpaceId;
        private long workspaceId;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder sharedSpaceId(long sharedSpaceId) {
            this.sharedSpaceId = sharedSpaceId;
            return this;
        }

        public Builder workspaceId(long workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        public BusinessRulesService build() {
            Collection<String> fieldsNotSet = new ArrayList<>();
            if (username == null) {
                fieldsNotSet.add("username");
            }
            if (password == null) {
                fieldsNotSet.add("password");
            }
            if (url == null) {
                fieldsNotSet.add("url");
            }
            if (sharedSpaceId <= 0) {
                fieldsNotSet.add("sharedSpaceId");
            }

            if (!fieldsNotSet.isEmpty()) {
                throw new IllegalArgumentException("Please set the following required fields: " + StringUtils.join(fieldsNotSet, ", "));
            }

            boolean onMasterWorkspace = false;
            if (workspaceId <= 0) {
                // use master workspace if not set
                workspaceId = MASTER_WORKSPACE_ID;
                onMasterWorkspace = true;
            }

            APIMode migrationAPIMode = new APIMode() {
                @Override
                public String getHeaderValue() {
                    return "OCTANE_MIGRATION";
                }

                @Override
                public String getHeaderKey() {
                    return "HPECLIENTTYPE";
                }
            };

            Octane octane = new Octane.Builder(new SimpleUserAuthentication(username, password, migrationAPIMode))
                    .Server(url)
                    .sharedSpace(sharedSpaceId)
                    .workSpace(workspaceId)
                    .build();

            return new BusinessRulesService(octane, onMasterWorkspace);
        }
    }
}
