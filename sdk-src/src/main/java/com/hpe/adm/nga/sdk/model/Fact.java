package com.hpe.adm.nga.sdk.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Fact {

    private String type;

    private List<String> pathElements;

    public Fact(String type, List<String> pathElements) {
        this.type = type;
        this.pathElements = pathElements;
    }

    public static Fact getFact(String factString) {
        int paranthesisIndex = factString.indexOf("(");
        if (paranthesisIndex > 0) {
            Pattern pathElementPattern = Pattern.compile("\\(\"(?<element>.+?)\"\\)");
            Matcher matcher = pathElementPattern.matcher(factString.substring(paranthesisIndex, factString.length()));
            List<String> pathElements = new ArrayList<>();

            while (matcher.find()) {
                pathElements.add(matcher.group("element"));
            }

            if (!pathElements.isEmpty()) {
                String factType = factString.substring(0, paranthesisIndex);
                return new Fact(factType, pathElements);
            }
        }

        return null;
    }

    public static Collection<Fact> extractFacts(EntityModel model) {
        Collection<Fact> facts = new ArrayList<>();

        model.getValues().forEach(fieldModel -> {
            if (fieldModel instanceof FactFieldModel) {
                facts.add(((FactFieldModel) fieldModel).getValue());
            } else if (fieldModel instanceof MultiReferenceFieldModel) {
                MultiReferenceFieldModel multiRefFieldModel = (MultiReferenceFieldModel) fieldModel;
                multiRefFieldModel.getValue().forEach(em -> facts.addAll(extractFacts(em)));
            } else if (fieldModel instanceof ReferenceFieldModel) {
                ReferenceFieldModel refFieldModel = (ReferenceFieldModel) fieldModel;
                facts.addAll(extractFacts(refFieldModel.getValue()));
            }
        });

        return facts;
    }

    public boolean replaceIdAfterPathSequence(String pathSequence, String initialId, String newId) {
        List<String> pathSequenceList = Arrays.asList(pathSequence.split("/"));
        int startIndex = Collections.indexOfSubList(pathElements, pathSequenceList);
        if (startIndex >= 0) {
            int idPosition = startIndex + pathSequenceList.size();
            if (pathElements.get(idPosition).equals(initialId)) {
                pathElements.set(idPosition, newId);

                return true;
            }
        }

        return false;
    }

    public String getType() {
        return type;
    }

    public List<String> getPathElements() {
        return pathElements;
    }

    @Override
    public String toString() {
        return type + StringUtils.join(pathElements.stream()
                .map(e -> "(\"" + e + "\")")
                .collect(Collectors.toList())
                .toArray());
    }
}
