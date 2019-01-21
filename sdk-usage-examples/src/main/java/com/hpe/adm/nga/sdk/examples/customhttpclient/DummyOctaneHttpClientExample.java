package com.hpe.adm.nga.sdk.examples.customhttpclient;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.OctaneClassFactory;
import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.authentication.SimpleUserAuthentication;
import com.hpe.adm.nga.sdk.model.EntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example to show how to use your own custom http client with the sdk
 */
public class DummyOctaneHttpClientExample {

    private static final Logger logger = LoggerFactory.getLogger(DummyOctaneHttpClientExample.class.getName());

    public static void main(String[] args) {

        Authentication authentication
                = new SimpleUserAuthentication("", "");

        int dummyDefectCount = ThreadLocalRandom.current().nextInt(0, 101);
        //Set the number of dummy entities the custom http client should make
        DummyOctaneHttpClient.dummyDefectCount = dummyDefectCount;

        Octane octane =
                new Octane.Builder(authentication, new DummyOctaneHttpClient(""))
                        .Server("")
                        .sharedSpace(-1)
                        .workSpace(-1)
                        .build();

        //Fetch defects as an example a print them to the console
        Collection<EntityModel> defects = octane.entityList("work_items").get().execute();
        defects.forEach(defect -> logger.info(defect.getValue("id").getValue() + " " + defect.getValue("name").getValue()));

        assert defects.size() == dummyDefectCount;

        //Remove system property
        System.getProperties().remove(OctaneClassFactory.OCTANE_CLASS_FACTORY_CLASS_NAME);
    }

}
