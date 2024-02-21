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
package com.hpe.adm.nga.sdk.extension.businessrules;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Fact {

    private String type;

    private List<String> pathElements;

    private Fact(String type, List<String> pathElements) {
        this.type = type;
        this.pathElements = pathElements;
    }

    public static Fact getFact(String factString) {
        int paranthesisIndex = factString.indexOf("(");
        if (paranthesisIndex > 0 && !factString.substring(0, paranthesisIndex).matches(".*\\s.*")) {
            Pattern pathElementPattern = Pattern.compile("\\(\"(?<element>[^\"]+?)\"\\)");
            Matcher matcher = pathElementPattern.matcher(factString.substring(paranthesisIndex));
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
