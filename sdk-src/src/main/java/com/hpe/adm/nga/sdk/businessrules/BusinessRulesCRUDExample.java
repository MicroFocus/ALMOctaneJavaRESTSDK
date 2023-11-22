package com.hpe.adm.nga.sdk.businessrules;

import com.hpe.adm.nga.sdk.entities.OctaneCollection;
import com.hpe.adm.nga.sdk.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BusinessRulesCRUDExample {

    public static void main(String[] args) {

        BusinessRulesService sourceService = new BusinessRulesService.Builder()
                .username("sa@nga")
                .password("Welcome1")
                .url("http://localhost:8080")
                .sharedSpaceId(1001)
                .workspaceId(11001).build();

        BusinessRulesService targetService = new BusinessRulesService.Builder()
                .username("mqm_rnd@hpe.com")
                .password("Password1")
                .url("https://pmo8.almoctane.com")
                .sharedSpaceId(1001)
                .workspaceId(16001).build();

        // fetch rules from the source workspace
        OctaneCollection<EntityModel> sourceTeamRules = sourceService.getBusinessRulesForEntityType("team");
        sourceTeamRules.addAll(sourceService.getBusinessRulesForEntityType("defect"));

        // modify rules
        sourceTeamRules.forEach(rule -> {
            FieldModel commentField = rule.getValue("comment");
            String comment = commentField instanceof EmptyFieldModel ? "" : ((StringFieldModel) commentField).getValue();
            rule.setValue(new StringFieldModel("comment", comment + " replicated"));
        });

        applyMappingsOnRules(sourceTeamRules, buildReferenceMap());

        // import rules in target workspace
        OctaneCollection<EntityModel> targetTeamRules = targetService.createBusinessRules(sourceTeamRules);

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

    private static void applyMappingsOnRules(Collection<EntityModel> rules, Map<String, Map<String, String>> map) {
        // collect facts from rules
        Collection<Fact> facts = new ArrayList<>();
        rules.forEach(rule -> facts.addAll(Fact.extractFacts(rule)));

        // make replacements
        map.forEach((refType, idMap) -> {
            idMap.forEach((sourceId, targetId) -> {
                facts.stream()
                        .filter(f -> f.getPathElements().contains(refType))
                        .forEach(fact -> {
                            int refTypeIndex = fact.getPathElements().indexOf(refType);
                            if (fact.getPathElements().get(refTypeIndex + 1).equals(sourceId)) {
                                fact.getPathElements().set(refTypeIndex + 1, targetId);
                            }
                        });
            });
        });
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
}