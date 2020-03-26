package com.hpe.adm.nga.sdk.manualtests.script;

import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.manualtests.teststeps.TestStepParser;
import com.hpe.adm.nga.sdk.model.EntityModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;

import javax.annotation.Nullable;
import java.util.List;

public class GetTestScriptModel extends TypedEntityModel {

    public GetTestScriptModel(final EntityModel wrappedEntityModel) {
        super(wrappedEntityModel);
    }

    @Nullable
    @Override
    public String getId() {
        return null;
    }

    /**
     * Label: Creation Time
     * Description: The date and time the script was created, according to the ISO-8601 date format.
     */
    public java.time.ZonedDateTime getCreationTime() {
        final FieldModel creation_time = wrappedEntityModel.getValue("creation_time");
        return creation_time == null ? null : (java.time.ZonedDateTime) creation_time.getValue();
    }

    /**
     * Label: Last modified
     * Description: The date and time the script was last modified, according to the ISO-8601 date format.
     */
    public java.time.ZonedDateTime getLastModified() {
        final FieldModel last_modified = wrappedEntityModel.getValue("last_modified");
        return last_modified == null ? null : (java.time.ZonedDateTime) last_modified.getValue();
    }

    public List<AbstractTestStep> getTestStepsAsObjects() {
        return TestStepParser.parseTestSteps(getTestStepsAsString());
    }

    public String getTestStepsAsString() {
        final StringFieldModel scriptFieldModel = (StringFieldModel) wrappedEntityModel.getValue("script");
        return scriptFieldModel.getValue();
    }
}
