/*
 * Copyright 2016-2023 Open Text.
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
package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.extension.businessrules.BusinessRuleEntityModel;
import com.hpe.adm.nga.sdk.extension.businessrules.BusinessRulesService;
import com.hpe.adm.nga.sdk.extension.businessrules.Fact;
import com.hpe.adm.nga.sdk.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BusinessRulesCRUDExample {

    public static void main(String[] args) {

        BusinessRulesService sourceService = new BusinessRulesService.Builder()
                .username("")
                .password("")
                .url("")
                .sharedSpaceId(0)
                .workspaceId(0).build();

        BusinessRulesService targetService = new BusinessRulesService.Builder()
                .username("")
                .password("")
                .url("")
                .sharedSpaceId(0)
                .workspaceId(0).build();

        // fetch rules from the source workspace
        Collection<BusinessRuleEntityModel> sourceTeamRules = sourceService.getBusinessRulesForEntityType("team");
        sourceTeamRules.addAll(sourceService.getBusinessRulesForEntityType("defect"));

        // modify rules
        sourceTeamRules.forEach(rule -> {
            FieldModel commentField = rule.getValue("comment");
            String comment = commentField instanceof EmptyFieldModel ? "" : ((StringFieldModel) commentField).getValue();
            rule.setValue(new StringFieldModel("comment", comment + " replicated"));
        });

        applyMappingsOnRules(sourceTeamRules, buildReferenceMap2());

        // import rules in target workspace
        Collection<BusinessRuleEntityModel> targetTeamRules = targetService.createBusinessRules(sourceTeamRules);

        // make changes on the target rules before updating them
        targetTeamRules.forEach(rule -> {
            String comment = ((StringFieldModel) rule.getValue("comment")).getValue();
            rule.setValue(new StringFieldModel("comment", comment + " and updated"));
        });

        // update rules on target workspace
        targetService.updateBusinessRules(targetTeamRules);

        // delete rules that were added on target
        targetService.deleteBusinessRules(targetTeamRules);
    }

    private static void applyMappingsOnRules(Collection<BusinessRuleEntityModel> rules, Map<String, Map<String, String>> map) {
        // collect facts from rules
        Collection<Fact> facts = new ArrayList<>();
        rules.forEach(rule -> facts.addAll(rule.getFacts()));

        AtomicInteger changes = new AtomicInteger();

        // make replacements
        map.forEach((refType, idMap) -> idMap.forEach((sourceId, targetId) -> facts.forEach(fact -> {
            if (fact.replaceIdAfterPathSequence(refType, sourceId, targetId)) {
                changes.incrementAndGet();
            }
        })));

        System.out.println("Changed " + changes.get() + " facts in rules");
    }

    private static Map<String, Map<String, String>> buildReferenceMap() {
        Map<String, Map<String, String>> map = new HashMap<>();

        Map<String, String> usersMap = new HashMap<>();
        usersMap.put("1001", "1001");
        usersMap.put("3001", "12006");
        usersMap.put("7001", "10002");
        map.put("workspace_user", usersMap);

        Map<String, String> programsMap = new HashMap<>();
        programsMap.put("2001", "6001");
        programsMap.put("2002", "6002");
        map.put("program", programsMap);

        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("color_source_udf", "color_target_udf");
        map.put("fields", fieldsMap);
        map.put("original-fields", fieldsMap);

        Map<String, String> listsMap = new HashMap<>();
        listsMap.put("z83ykl1gvv03ebpxdolqg75r9", "gd63y31jr26wrioze3p4q7kq0"); // Color List
        listsMap.put("lqrz78gq01lvlaxp1p900k6j5", "5oq47q8qnw698cp8ejgv87gmr"); // red
        listsMap.put("p516m2lo0v493i3j9errkwno4", "5oq47qn05v58honq41zn7gmrj"); // green
        listsMap.put("xgw8m03940380f5x3j62dm45n", "d638ld9y8pgp1ad0nvwdx2x1o"); // blue
        map.put("lists", listsMap);

        return map;
    }

    private static Map<String, Map<String, String>> buildReferenceMap2() {
        Map<String, Map<String, String>> map = new HashMap<>();

        Map<String, String> usersMap = new HashMap<>();
        usersMap.put("1001", "1001");
        usersMap.put("3001", "12006");
        usersMap.put("7001", "10002");
        map.put("entity/workspace_user", usersMap);

        Map<String, String> programsMap = new HashMap<>();
        programsMap.put("2001", "6001");
        programsMap.put("2002", "6002");
        map.put("entity/program", programsMap);

        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("color_source_udf", "color_target_udf");
        map.put("work_item/fields", fieldsMap);
        map.put("work_item/original-fields", fieldsMap);
        map.put("work_item/customization/fields", fieldsMap);

        Map<String, String> listsMap = new HashMap<>();
        listsMap.put("z83ykl1gvv03ebpxdolqg75r9", "gd63y31jr26wrioze3p4q7kq0"); // Color List
        listsMap.put("lqrz78gq01lvlaxp1p900k6j5", "5oq47q8qnw698cp8ejgv87gmr"); // red
        listsMap.put("p516m2lo0v493i3j9errkwno4", "5oq47qn05v58honq41zn7gmrj"); // green
        listsMap.put("xgw8m03940380f5x3j62dm45n", "d638ld9y8pgp1ad0nvwdx2x1o"); // blue
        listsMap.put("list_node.automation_status.ready_for_automation", "list_node.automation_status.requires_update");
        map.put("lists", listsMap);

        return map;
    }
}
