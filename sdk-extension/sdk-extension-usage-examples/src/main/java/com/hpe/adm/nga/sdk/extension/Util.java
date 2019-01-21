package com.hpe.adm.nga.sdk.extension;

import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class.getName());

    /**
     * Simple debug method used to print a list of EntityModel objects to the console
     * For each entity, it will try to print the type + the fields [name] or [text]
     * If the fields that it tries to print do not exists, it will only print the type of the entity
     * @param entities {@link Collection} of {@link EntityModel}s to print
     */
    public static void printEntities(Collection<EntityModel> entities) {
        logger.info("Collection size: " + entities.size());
        if (entities.size() != 0) {
            entities
                .stream()
                .map(entityModel -> {
                    String entityType = entityModel.getValue("type").getValue().toString().toUpperCase();
                    String[] printFieldNames = new String[]{"name", "text"};

                    for(String fieldName : printFieldNames) {
                        FieldModel printFieldModel = entityModel.getValue(fieldName);
                        if (printFieldModel != null) {
                            return entityType+":"+printFieldModel.getValue().toString();
                        }
                    }
                    return entityType;
                })
                .forEach(logger::info);
        }
    }

}
