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
package com.hpe.adm.nga.sdk.generate;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.metadata.features.Feature;
import com.hpe.adm.nga.sdk.metadata.features.RestFeature;
import com.hpe.adm.nga.sdk.metadata.features.SubTypesOfFeature;
import com.hpe.adm.nga.sdk.model.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>The class that generates entities based on the metadata from the given ALM Octane server
 * This class generates models based on the {@link com.hpe.adm.nga.sdk.model.TypedEntityModel},
 * entity lists based on {@link com.hpe.adm.nga.sdk.entities.TypedEntityList} and Lists &amp; Phases objects
 * which represents those entities on the server and turns them into typed enums.
 * </p>
 * <p>
 * The user that calls the generation must have the workspace member of the given workspace.
 * </p>
 * <p>
 * UDFs are generated if they are part of the metadata for that workspace.  That means that the generated
 * entities should be able to be reused over different workspaces within the same shared space.  However
 * some business rules could cause different behaviour in different Workspaces.  See the ALM Octane documentation
 * for more information
 * </p>
 */
public class GenerateModels {

    private final Template template, interfaceTemplate, entityListTemplate, phasesTemplate, listsTemplate;
    private final File modelDirectory, entitiesDirectory, enumsDirectory;

    /**
     * Initialise the class with the output directory.  This should normally be in a project that would be
     * imported into the main Java project
     *
     * @param outputDirectory Where all the generated files will be placed
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public GenerateModels(final File outputDirectory) {
        final File packageDirectory = new File(outputDirectory, "/com/hpe/adm/nga/sdk");
        modelDirectory = new File(packageDirectory, "model");
        modelDirectory.mkdirs();
        entitiesDirectory = new File(packageDirectory, "entities");
        entitiesDirectory.mkdirs();
        enumsDirectory = new File(packageDirectory, "enums");
        enumsDirectory.mkdirs();

        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new SLF4JLogChute());
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine.init();

        template = velocityEngine.getTemplate("/EntityModel.vm");
        interfaceTemplate = velocityEngine.getTemplate("/Entity.vm");
        entityListTemplate = velocityEngine.getTemplate("/TypedEntityList.vm");
        phasesTemplate = velocityEngine.getTemplate("/Phases.vm");
        listsTemplate = velocityEngine.getTemplate("/Lists.vm");
    }

    /**
     * Run the actual generation
     *
     * @param authentication The authentication object
     * @param server         The server including the protocol and port
     * @param sharedSpace    The SS id
     * @param workSpace      The WS id
     * @throws IOException A problem with the generation of the entities
     */
    public void generate(Authentication authentication, String server, long sharedSpace, long workSpace) throws IOException {
        final Octane octane = new Octane.Builder(authentication).sharedSpace(sharedSpace).workSpace(workSpace).Server(server).build();
        final Metadata metadata = octane.metadata();
        final Collection<EntityMetadata> entityMetadata = metadata.entities().execute();

        final Map<String, String> logicalNameToListsMap = generateLists(octane);
        final Set<String> availablePhases = generatePhases(octane);

        for (EntityMetadata entityMetadatum : entityMetadata) {
            final String name = entityMetadatum.getName();
            if (entityShouldNotBeGenerated(name)) continue;

            final String interfaceName = GeneratorHelper.camelCaseFieldName(name) + "Entity";
            final Collection<FieldMetadata> fieldMetadata = generateEntity(metadata, entityMetadata, entityMetadatum, name, interfaceName, logicalNameToListsMap, availablePhases);
            generateInterface(entityMetadatum, name, interfaceName);
            generateEntityList(entityMetadatum, name, fieldMetadata);
        }
    }

    /**
     * There are a few fields that cannot be generated due to inconsistencies.  These could haev special cases but it is simpler to exclude them
     * from generation.  If there is a problem then they can be checked on an individual basis
     *
     * @param name The entity that should be checked
     * @return Whether this entity should be ignored and therefore not generated
     */
    private boolean entityShouldNotBeGenerated(String name) {
        /*
         * @Since 15.0.20
         * The run_history's id is integer even though it should be string.  It would be extremely complicated to make a special case for run_history id as long
         * Therefore until this is fixed in Octane - the entity will be ignored
         */
        if (name.equals("run_history")) {
            return true;
        }

        /*
         * @Since 15.1.20
         * field_metadata is a special case in that it is used when defining UDFs.  It causes problems in the entity generation due to the list node
         * not having a reference.  It is unlikely that this would be used by the SDK so is ignored for now.  If this does cause an issue we could
         * look into fixing this in the future
         */
        if (name.startsWith("field_metadata")) {
            return true;
        }

        /*
         * @Since 15.1.20
         * log entities have the ID marked as an integer and not as a string.  This causes issues with creating the entity.
         * A defect has been raised in Octane to fix this
         */
        if (name.startsWith("log")) {
            return true;
        }

        /*
         * @Since 15.1.20
         * This entity "overrides" the type field for its own use which causes issues.
         * A defect has been raised in Octane to fix this
         */
        return name.equals("ci_parameter");
    }

    private Map<String, String> generateLists(Octane octane) throws IOException {
        final Collection<EntityModel> listNodes = octane.entityList("list_nodes").get().addFields("name", "list_root", "id", "logical_name").execute();
        final Map<String, List<String[]>> mappedListNodes = new HashMap<>();
        final Map<String, String> logicalNameToNameMap = new HashMap<>();
        listNodes.forEach(listNode -> {
            final String rootId;

            final FieldModel<?> listRootFieldModel = listNode.getValue("list_root");
            final String name;
            if (listRootFieldModel instanceof EmptyFieldModel) {
                rootId = listNode.getId();
                name = GeneratorHelper.camelCaseFieldName(GeneratorHelper.getJavaCompliantIdentifier(((StringFieldModel) listNode.getValue("name")).getValue()));
                logicalNameToNameMap.put(((StringFieldModel) listNode.getValue("logical_name")).getValue(), getPackageForList(rootId).concat(".").concat(name));
            } else {
                final EntityModel list_rootValue = ((ReferenceFieldModel) listRootFieldModel).getValue();
                rootId = list_rootValue.getId();
                name = GeneratorHelper.getJavaCompliantIdentifier(((StringFieldModel) listNode.getValue("name")).getValue()).toUpperCase();
            }
            final List<String[]> listHierarchy = mappedListNodes.computeIfAbsent(rootId, k -> new ArrayList<>());

            final String[] listNodeInfo = {name, ((StringFieldModel) listNode.getValue("id")).getValue()};
            if (listRootFieldModel instanceof EmptyFieldModel) {
                listHierarchy.add(0, listNodeInfo);
            } else {
                listHierarchy.add(listNodeInfo);
            }
        });

        for (final Map.Entry<String, List<String[]>> entry : mappedListNodes.entrySet()) {
            final String rootId = entry.getKey();
            final List<String[]> nodes = entry.getValue();
            final String className = nodes.get(0)[0];

            final Path listDirectoryPath = enumsDirectory.toPath().resolve(Paths.get("lists", getPackageForRootIdForList(rootId).split("\\.")));
            final File listDirectoryPathFile = listDirectoryPath.toFile();
            //noinspection ResultOfMethodCallIgnored
            listDirectoryPathFile.mkdirs();
            final File listFile = new File(listDirectoryPathFile, className + ".java");

            final VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("listItemsSet", entry);
            velocityContext.put("className", className);
            velocityContext.put("packageName", getPackageForList(rootId));
            final FileWriter fileWriter = new FileWriter(listFile);
            listsTemplate.merge(velocityContext, fileWriter);
            fileWriter.close();
        }

        return logicalNameToNameMap;
    }

    private String getPackageForList(final String rootId) {
        return "com.hpe.adm.nga.sdk.enums.lists.".concat(getPackageForRootIdForList(rootId));
    }

    private String getPackageForRootIdForList(String rootId) {
        final String[] splitRootIds = rootId.split("\\.");
        final StringBuilder packageStringBuilder = new StringBuilder();
        for (int i = 0; i < splitRootIds.length - 1; i++) {
            final String splitRootId = getPackagePrefixForLists(splitRootIds[i]);
            packageStringBuilder.append(splitRootId).append(".");
        }
        packageStringBuilder.append(getPackagePrefixForLists(splitRootIds[splitRootIds.length - 1]));

        return packageStringBuilder.toString();
    }

    private String getPackagePrefixForLists(String packageName) {
        return Character.isJavaIdentifierStart(packageName.charAt(0)) ? packageName : "_" + packageName;
    }

    private Set<String> generatePhases(Octane octane) throws IOException {
        final Map<String, Set<String[]>> phaseMap = new HashMap<>();
        final Collection<EntityModel> phases = octane.entityList("phases").get().addFields("id", "name", "entity").execute();
        phases.forEach(phase -> {
            final Set<String[]> phaseValueSet = new HashSet<>();
            phaseValueSet.add(new String[]{phase.getId(), ((StringFieldModel) phase.getValue("name")).getValue()
                    .replaceAll(" ", "_")
                    .replaceAll("&", "N")
                    .toUpperCase()});
            phaseMap.merge(GeneratorHelper.camelCaseFieldName(((StringFieldModel) phase.getValue("entity")).getValue(), true), phaseValueSet, (existingValues, newValues) -> {
                existingValues.addAll(newValues);
                return existingValues;
            });
        });

        final VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("phaseMap", phaseMap);
        final FileWriter fileWriter = new FileWriter(new File(enumsDirectory, "Phases.java"));
        phasesTemplate.merge(velocityContext, fileWriter);
        fileWriter.close();

        return phaseMap.keySet();
    }

    private Collection<FieldMetadata> generateEntity(Metadata metadata, Collection<EntityMetadata> entityMetadata, EntityMetadata entityMetadatum, String name, String interfaceName, Map<String, String> logicalNameToListsMap, Set<String> availablePhases) throws IOException {
        final Collection<FieldMetadata> fieldMetadata = sanitiseMetaData(metadata.fields(name).execute());

        final TreeMap<String, List<String>> collectedReferences = fieldMetadata.stream()
                .filter(FieldMetadata::isRequired)
                .collect(Collectors.toMap(FieldMetadata::getName, fieldMetadata1 -> {
                    final List<String> references = new ArrayList<>();
                    final String className = GeneratorHelper.camelCaseFieldName(entityMetadatum.getName());
                    if (fieldMetadata1.getName().equals("phase") && availablePhases.contains(className)) {
                        references.add("com.hpe.adm.nga.sdk.enums.Phases." + className + "Phase");
                    } else if (fieldMetadata1.getFieldType() == FieldMetadata.FieldType.Reference) {
                        if ((!entityMetadatum.getName().equals("list_node")) && (fieldMetadata1.getFieldTypedata().getTargets()[0].getType().equals("list_node"))) {
                            final String listName = logicalNameToListsMap.get(fieldMetadata1.getFieldTypedata().getTargets()[0].logicalName());
                            references.add(listName);
                        } else {
                            final GeneratorHelper.ReferenceMetadata referenceMetadata = GeneratorHelper.getAllowedSuperTypesForReference(fieldMetadata1, entityMetadata);
                            if (fieldMetadata1.getFieldTypedata().isMultiple()) {
                                assert referenceMetadata != null;
                                references.add(referenceMetadata.getReferenceClassForSignature());
                            } else {
                                assert referenceMetadata != null;
                                if (referenceMetadata.hasTypedReturn()) {
                                    references.addAll(referenceMetadata.getReferenceTypes()
                                            .stream()
                                            .map(type -> GeneratorHelper.camelCaseFieldName(type).concat("EntityModel"))
                                            .collect(Collectors.toSet()));
                                }
                                if (referenceMetadata.hasNonTypedReturn()) {
                                    references.add("EntityModel");
                                }
                            }
                        }
                    } else {
                        references.add(GeneratorHelper.getFieldTypeAsJava(fieldMetadata1.getFieldType()));
                    }

                    return references;
                }, (strings, strings2) -> {
                    throw new IllegalStateException("problem merging map");
                }, TreeMap::new));

        final Set<List<String[]>> requiredFields = new HashSet<>();
        if (!collectedReferences.isEmpty()) {
            expandCollectedReferences(collectedReferences, new int[collectedReferences.size()], 0, requiredFields);
        }

        final VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("interfaceName", interfaceName);
        velocityContext.put("entityMetadata", entityMetadatum);
        velocityContext.put("fieldMetadata", fieldMetadata);
        velocityContext.put("logicalNameToListsMap", logicalNameToListsMap);
        velocityContext.put("entityMetadataCollection", entityMetadata);
        velocityContext.put("GeneratorHelper", GeneratorHelper.class);
        velocityContext.put("entityMetadataWrapper", GeneratorHelper.entityMetadataWrapper(entityMetadatum));
        velocityContext.put("availablePhases", availablePhases);
        velocityContext.put("requiredFields", requiredFields);

        final FileWriter fileWriter = new FileWriter(new File(modelDirectory, GeneratorHelper.camelCaseFieldName(name) + "EntityModel.java"));
        template.merge(velocityContext, fileWriter);

        fileWriter.close();
        return fieldMetadata;
    }

    private Collection<FieldMetadata> sanitiseMetaData(Collection<FieldMetadata> fieldMetadataCollection) {
        return fieldMetadataCollection.stream()
                // filter out entities that have references without a target - like public to protected
                .filter(fieldMetadata -> fieldMetadata.getFieldType() != FieldMetadata.FieldType.Reference || fieldMetadata.getFieldTypedata().getTargets() != null)
                .collect(Collectors.toList());
    }

    private void expandCollectedReferences(final TreeMap<String, List<String>> collectedReferences, final int[] positions, final int pointer, final Set<List<String[]>> output) {
        final String[] keyArray = collectedReferences.keySet().toArray(new String[0]);
        final String o = keyArray[pointer];
        for (int i = 0; i < collectedReferences.get(o).size(); ++i) {
            if (pointer == positions.length - 1) {
                final List<String[]> outputLine = new ArrayList<>(positions.length);
                for (int j = 0; j < positions.length; ++j) {
                    outputLine.add(new String[]{keyArray[j], collectedReferences.get(keyArray[j]).get(positions[j])});
                }
                output.add(outputLine);
            } else {
                expandCollectedReferences(collectedReferences, positions, pointer + 1, output);
            }
            positions[pointer]++;
        }
        positions[pointer] = 0;
    }

    private void generateInterface(EntityMetadata entityMetadatum, String name, String interfaceName) throws IOException {
        // interface
        final VelocityContext interfaceVelocityContext = new VelocityContext();
        final Optional<Feature> subTypeOfFeature = entityMetadatum.features().stream().filter(feature -> feature instanceof SubTypesOfFeature).findAny();

        interfaceVelocityContext.put("interfaceName", interfaceName);
        interfaceVelocityContext.put("superInterfaceName",
                (subTypeOfFeature.map(feature -> GeneratorHelper.camelCaseFieldName(((SubTypesOfFeature) feature).getType())).orElse("")) + "Entity");

        final FileWriter interfaceFileWriter = new FileWriter(new File(modelDirectory, GeneratorHelper.camelCaseFieldName(name) + "Entity.java"));
        interfaceTemplate.merge(interfaceVelocityContext, interfaceFileWriter);

        interfaceFileWriter.close();
    }

    private void generateEntityList(EntityMetadata entityMetadatum, String name, Collection<FieldMetadata> fieldMetadata) throws IOException {
        // entityList
        final Optional<Feature> hasRestFeature = entityMetadatum.features().stream()
                .filter(feature -> feature instanceof RestFeature)
                .findFirst();
        // if not then something is wrong!
        if (hasRestFeature.isPresent()) {
            final RestFeature restFeature = (RestFeature) hasRestFeature.get();

            final VelocityContext entityListVelocityContext = new VelocityContext();
            entityListVelocityContext.put("helper", GeneratorHelper.class);
            entityListVelocityContext.put("type", GeneratorHelper.camelCaseFieldName(name));
            entityListVelocityContext.put("url", restFeature.getUrl());
            entityListVelocityContext.put("availableFields", fieldMetadata.stream().map(FieldMetadata::getName).collect(Collectors.toList()));
            entityListVelocityContext.put("sortableFields", fieldMetadata.stream().filter(FieldMetadata::isSortable).map(FieldMetadata::getName).collect(Collectors.toList()));

            final String[] restFeatureMethods = restFeature.getMethods();
            for (String restFeatureMethod : restFeatureMethods) {
                switch (restFeatureMethod) {
                    case "GET":
                        entityListVelocityContext.put("hasGet", true);
                        break;
                    case "POST":
                        entityListVelocityContext.put("hasCreate", true);
                        break;
                    case "PUT":
                        entityListVelocityContext.put("hasUpdate", true);
                        break;
                    case "DELETE":
                        entityListVelocityContext.put("hasDelete", true);
                        break;
                }
            }

            // add test script support for test_manual or gherkin tests only
            if (name.equals("test_manual") || name.equals("gherkin_tests")) {
                entityListVelocityContext.put("hasTestScript", true);
            }

            final FileWriter entityListFileWriter = new FileWriter(new File(entitiesDirectory, GeneratorHelper.camelCaseFieldName(name) + "EntityList.java"));
            entityListTemplate.merge(entityListVelocityContext, entityListFileWriter);

            entityListFileWriter.close();
        }
    }
}