package com.hpe.adm.nga.sdk.unit_tests.common;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.entities.EntityList;
import com.hpe.adm.nga.sdk.entities.TypedEntityList;
import com.hpe.adm.nga.sdk.model.ErrorModel;
import com.hpe.adm.nga.sdk.model.FieldModel;
import com.hpe.adm.nga.sdk.model.MultiReferenceFieldModel;
import com.hpe.adm.nga.sdk.model.ReferenceFieldModel;
import com.hpe.adm.nga.sdk.network.OctaneHttpClient;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.Collection;
import java.util.Set;

public class CommonMethods {

    private final static String urlDomain = "http://localhost:8080";
    private final static String sharedSpace = "1001";
    private final static int workSpace = 1002;

    public static Octane getOctaneForTest() {
        final OctaneHttpClient octaneHttpClient = PowerMockito.mock(OctaneHttpClient.class);
        PowerMockito.when(octaneHttpClient.authenticate(Mockito.any())).thenReturn(true);

        return new Octane.Builder(new SimpleUserAuthentication("user", "password"), octaneHttpClient)
                .Server(getDomain())
                .sharedSpace(Long.parseLong(getSharedSpace()))
                .workSpace(getWorkSpace())
                .build();
    }

    public static String getDomain() {
        return urlDomain;
    }

    public static String getSharedSpace() {
        return sharedSpace;
    }

    public static int getWorkSpace() {
        return workSpace;
    }


    public static boolean isErrorAInErrorB(ErrorModel entityA, ErrorModel entityB) {
        if (entityA == null) {
            return true;
        }
        Set<FieldModel> fieldsA = entityA.getValues();
        Set<FieldModel> fieldsB = entityB.getValues();
        boolean isMatch;
        for (FieldModel fieldA : fieldsA) {
            if (fieldA.getClass().equals(MultiReferenceFieldModel.class) || fieldA.getClass().equals(ReferenceFieldModel.class)) {
                continue;
            }
            isMatch = false;
            for (FieldModel fieldB : fieldsB) {
                if (fieldA.getName().equals(fieldB.getName())
                        && (fieldA.getValue() == null
                        || fieldA.getValue().equals(fieldB.getValue()))) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }

    public static boolean isErrorCollectionAInErrorCollectionB(Collection<ErrorModel> collectionA, Collection<ErrorModel> collectionB) {
        boolean isMatch;
        for (ErrorModel entityA : collectionA) {
            isMatch = false;
            for (ErrorModel entityB : collectionB) {
                if (isErrorAInErrorB(entityA, entityB)) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return false;
            }
        }
        return true;
    }

}