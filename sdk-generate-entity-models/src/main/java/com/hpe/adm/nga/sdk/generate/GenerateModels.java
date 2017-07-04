package com.hpe.adm.nga.sdk.generate;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.metadata.features.Feature;
import com.hpe.adm.nga.sdk.metadata.features.RestFeature;
import com.hpe.adm.nga.sdk.metadata.features.SubTypesOfFeature;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by brucesp on 21-Jun-17.
 */
public class GenerateModels {

    public static void main(String[] args) throws Exception {

        File oytDir = new File("C:\\dev\\java-rest-sdk\\sdk-generate-entity-models\\target\\generated-sources/com/hpe/adm/nga/sdk/model/");
        oytDir.mkdirs();
        File pytDir = new File("C:\\dev\\java-rest-sdk\\sdk-generate-entity-models\\target\\generated-sources/com/hpe/adm/nga/sdk/entities/");
        pytDir.mkdirs();

        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "root");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine.init();

        final Template template = velocityEngine.getTemplate("/EntityModel.vm");
        final Template interfaceTemplate = velocityEngine.getTemplate("/Entity.vm");
        final Template entityListTemplate = velocityEngine.getTemplate("/TypedEntityList.vm");

        // work around for work_items_root
        final Octane octanePrivate = new Octane.Builder(new SimpleClientAuthentication("test_nd6l5z13vd1lztjxe9zp4oqe0", "+169663f176b79ddA", "HPE_REST_API_TECH_PREVIEW")).sharedSpace(20017).workSpace(4001).Server("https://mqast001pngx.saas.hpe.com").build();
        final EntityMetadata work_items_root = octanePrivate.metadata().entities("work_item_root").execute().iterator().next();
        final Collection<FieldMetadata> work_items_rootFields = octanePrivate.metadata().fields("work_item_root").execute();
        octanePrivate.signOut();

        final Octane octane = new Octane.Builder(new SimpleClientAuthentication("test_nd6l5z13vd1lztjxe9zp4oqe0", "+169663f176b79ddA")).sharedSpace(20017).workSpace(4001).Server("https://mqast001pngx.saas.hpe.com").build();
        final Metadata metadata = octane.metadata();
        final Collection<EntityMetadata> entityMetadata = metadata.entities().execute();
        entityMetadata.add(work_items_root);

        for (EntityMetadata entityMetadatum : entityMetadata) {
            final String name = entityMetadatum.getName();
            final String interfaceName = GeneratorHelper.camelCaseFieldName(name) + "Entity";
            //if (!name.equals("run")) continue;
            System.out.println(name + ":");
            final Collection<FieldMetadata> fieldMetadata = name.equals("work_item_root") ? work_items_rootFields : metadata.fields(name).execute();

            final VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("interfaceName", interfaceName);
            velocityContext.put("entityMetadata", entityMetadatum);
            velocityContext.put("fieldMetadata", fieldMetadata);
            velocityContext.put("entityMetadataCollection", entityMetadata);
            velocityContext.put("GeneratorHelper", GeneratorHelper.class);
            velocityContext.put("entityMetadataWrapper", GeneratorHelper.entityMetadataWrapper(entityMetadatum));

            final FileWriter fileWriter = new FileWriter(new File(oytDir, GeneratorHelper.camelCaseFieldName(name) + "EntityModel.java"));
            template.merge(velocityContext, fileWriter);

            fileWriter.close();

            // interface
            final VelocityContext interfaceVelocityContext = new VelocityContext();
            final Optional<Feature> subTypeOfFeature = entityMetadatum.features().stream().filter(feature -> feature instanceof SubTypesOfFeature).findAny();

            interfaceVelocityContext.put("interfaceName", interfaceName);
            interfaceVelocityContext.put("superInterfaceName",
                    (subTypeOfFeature.map(feature -> GeneratorHelper.camelCaseFieldName(((SubTypesOfFeature) feature).getType())).orElse("")) + "Entity");

            final FileWriter interfaceFileWriter = new FileWriter(new File(oytDir, GeneratorHelper.camelCaseFieldName(name) + "Entity.java"));
            interfaceTemplate.merge(interfaceVelocityContext, interfaceFileWriter);

            interfaceFileWriter.close();

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

                final FileWriter entityListFileWriter = new FileWriter(new File(pytDir, GeneratorHelper.camelCaseFieldName(name) + "EntityList.java"));
                entityListTemplate.merge(entityListVelocityContext, entityListFileWriter);

                entityListFileWriter.close();
            }

        }

    }


}
