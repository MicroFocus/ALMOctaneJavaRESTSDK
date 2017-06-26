package com.hpe.adm.nga.sdk.generate;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.authentication.SimpleClientAuthentication;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

/**
 * Created by brucesp on 21-Jun-17.
 */
public class GenerateModels {

    public static void main(String[] args) throws Exception {

        File oytDir = new File("C:\\dev\\java-rest-sdk\\sdk-generate-entity-models\\target\\generated-sources/com/hpe/adm/nga/sdk/model/");
        oytDir.mkdirs();

        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
        velocityEngine.setProperty( "runtime.log.logsystem.log4j.logger", "root" );
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine.init();

        final Template template = velocityEngine.getTemplate("/EntityModel.vm");

        final Octane octane = new Octane.Builder(new SimpleClientAuthentication("test_nd6l5z13vd1lztjxe9zp4oqe0", "+169663f176b79ddA")).sharedSpace(20017).workSpace(4001).Server("https://mqast001pngx.saas.hpe.com").build();
        final Metadata metadata = octane.metadata();
        final Collection<EntityMetadata> entityMetadata = metadata.entities().execute();
        for (EntityMetadata entityMetadatum : entityMetadata) {
            final String name = entityMetadatum.getName();
            //if (!name.equals("defect")) continue;
            System.out.println(name + ":");
            final Collection<FieldMetadata> fieldMetadata = metadata.fields(name).execute();

            final VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("entityMetadata", entityMetadatum);
            velocityContext.put("fieldMetadata", fieldMetadata);
            velocityContext.put("entityMetadataCollection", entityMetadata);
            velocityContext.put("GeneratorHelper", GeneratorHelper.class);
            velocityContext.put("entityMetadataWrapper", GeneratorHelper.entityMetadataWrapper(entityMetadatum));

            final FileWriter fileWriter = new FileWriter(new File(oytDir, GeneratorHelper.camelCaseFieldName(name) + "EntityModel.java"));
            template.merge(velocityContext, fileWriter);

            fileWriter.close();
//             if (1==1)break;
//            for (FieldMetadata fieldMetadatum : fieldMetadata) {
//                System.out.println("\t"+fieldMetadatum.getName()+";"+fieldMetadatum.getFieldType().name());
//                if (fieldMetadatum.getFieldType() == FieldMetadata.FieldType.Reference) {
//                    final FieldMetadata.FieldTypeData fieldTypedata = fieldMetadatum.getFieldTypedata();
//                    final FieldMetadata.Target[] targets = fieldTypedata.getTargets();
//                }
//            }

        }

    }


}
