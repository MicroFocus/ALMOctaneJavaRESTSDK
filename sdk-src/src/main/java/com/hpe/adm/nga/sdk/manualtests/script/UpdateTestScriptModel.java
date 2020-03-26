package com.hpe.adm.nga.sdk.manualtests.script;

import com.hpe.adm.nga.sdk.manualtests.teststeps.AbstractTestStep;
import com.hpe.adm.nga.sdk.model.StringFieldModel;
import com.hpe.adm.nga.sdk.model.TypedEntityModel;

import javax.annotation.Nullable;
import java.util.List;

public final class UpdateTestScriptModel extends TypedEntityModel {

    @Nullable
    @Override
    public String getId() {
        return null;
    }

    enum TestScriptRevisionType {
        MINOR("Minor"), MAJOR("Major");

        private final String type;

        TestScriptRevisionType(String type) {
            this.type = type;
        }
    }

    public UpdateTestScriptModel setTestSteps(List<AbstractTestStep> testSteps) {
        if (testSteps != null) {
            final StringBuilder stepBuilder = new StringBuilder();
            testSteps.forEach(testStep ->
                    stepBuilder.append(testStep.getTestStepString())
            );
            setTestSteps(stepBuilder.toString());
        }

        return this;
    }

    public final UpdateTestScriptModel setTestSteps(String testSteps) {
        wrappedEntityModel.setValue(new StringFieldModel("script", testSteps));
        return this;
    }

    public final UpdateTestScriptModel setComment(String comment) {
        wrappedEntityModel.setValue(new StringFieldModel("comment", comment));
        return this;
    }

    public final UpdateTestScriptModel setRevisionType(final TestScriptRevisionType testScriptRevisionType) {
        wrappedEntityModel.setValue(new StringFieldModel("revision_type", testScriptRevisionType.type));
        return this;
    }
}
