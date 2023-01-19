/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
package com.hpe.adm.nga.sdk.tests.generate;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.entities.get.GetTypedEntity;
import com.hpe.adm.nga.sdk.generate.GenerateModels;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.CommonUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import com.hpe.adm.nga.sdk.utils.ContextUtils;
import com.hpe.adm.nga.sdk.utils.generator.DataGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class TestGenerateModels {

    private static final String TEST_NAME = "testName";
    private static final String TEST_DESCRIPTION = "testDescription";

    @Test
    public void testGenerateModels() throws Exception {
        final File generatedSourcesDirectory = new File("target/test-test-sources");
        FileUtils.deleteDirectory(generatedSourcesDirectory);
        //noinspection ResultOfMethodCallIgnored
        generatedSourcesDirectory.mkdirs();

        generateSources(generatedSourcesDirectory);
        compileClasses(generatedSourcesDirectory);
        testGeneratedClass(generatedSourcesDirectory);
    }

    private void compileClasses(File generatedDirectory) {
        final Collection<File> files = FileUtils.listFiles(generatedDirectory, null, true);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        List<String> optionList = new ArrayList<>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path"));

        Iterable<? extends JavaFileObject> compilationUnit
                = fileManager.getJavaFileObjectsFromFiles(files);
        compiler.getTask(
                null,
                fileManager,
                diagnostics,
                optionList,
                null,
                compilationUnit).call();

        boolean hasError = false;
        final StringBuilder stringBuilder = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                hasError = true;
                stringBuilder.append(String.format("Error on line %d in %s: %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri(),
                        diagnostic.getMessage(Locale.getDefault())));
            }
        }

        if (hasError) {
            Assert.fail(stringBuilder.toString());
        }
    }

    private void generateSources(File generatedDirectory) {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        final String url = configuration.getString("sdk.url");
        final Authentication authentication = AuthenticationUtils.getAuthentication(false);
        final String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        final String workspaceId = configuration.getString("sdk.workspaceId");

        try {
            new GenerateModels(generatedDirectory).generate(authentication, url, Long.parseLong(sharedSpaceId), Long.parseLong(workspaceId));
        } catch (Exception e) {
            Assert.fail("Test failed whilst building test sources; " + e.getMessage());
        }
    }

    private void testGeneratedClass(File generatedDirectory) throws Exception {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        final String url = configuration.getString("sdk.url");
        final Authentication authentication = AuthenticationUtils.getAuthentication(false);
        final String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        final String workspaceId = configuration.getString("sdk.workspaceId");

        final Octane octane = ContextUtils.getContextWorkspace(url, authentication, sharedSpaceId, workspaceId);

        final Collection<EntityModel> generatedEntity = DataGenerator.generateEntityModel(octane, "defects", new HashSet<>());
        final Collection<EntityModel> entityModels = octane.entityList("defects").create().entities(generatedEntity).execute();
        EntityModel entityModel = entityModels.iterator().next();
        final String entityId = CommonUtils.getIdFromEntityModel(entityModel);
        final String entityName = CommonUtils.getValueFromEntityModel(generatedEntity.iterator().next(), "name");

        final URLClassLoader classLoader = new URLClassLoader(new URL[]{generatedDirectory.toURI().toURL()}, this.getClass().getClassLoader());
        classLoader.clearAssertionStatus();
        final Class defectEntityListClass = classLoader.loadClass("com.hpe.adm.nga.sdk.entities.DefectEntityList");

        final TypedEntityList typedEntityList = octane.entityList(defectEntityListClass);

        final Object defectEntitiesObject = typedEntityList.getClass().getMethod("at", String.class).invoke(typedEntityList, entityId);

        final GetTypedEntity getDefectEntityModel = (GetTypedEntity) defectEntitiesObject.getClass().getMethod("get").invoke(defectEntitiesObject);
        final Class defectEntityAvailableFieldsEnumClass = classLoader.loadClass("com.hpe.adm.nga.sdk.entities.DefectEntityList$AvailableFields");
        final TypedEntityList.AvailableFields nameDefectField = (TypedEntityList.AvailableFields) Enum.valueOf(defectEntityAvailableFieldsEnumClass, "NAME");
        final TypedEntityList.AvailableFields authorDefectField = (TypedEntityList.AvailableFields) Enum.valueOf(defectEntityAvailableFieldsEnumClass, "AUTHOR");

        getDefectEntityModel.addFields(nameDefectField, authorDefectField);

        final TypedEntityModel defectTypedEntityModel = getDefectEntityModel.execute();
        final String getName = (String) defectTypedEntityModel.getClass().getMethod("getName").invoke(defectTypedEntityModel);
        final String getDescription = (String) defectTypedEntityModel.getClass().getMethod("getDescription").invoke(defectTypedEntityModel);
        final Object getAuthor = defectTypedEntityModel.getClass().getMethod("getAuthor").invoke(defectTypedEntityModel);

        Assert.assertEquals(entityName, getName);
        Assert.assertNull(getDescription);
        Assert.assertNotNull(getAuthor);
    }
}
