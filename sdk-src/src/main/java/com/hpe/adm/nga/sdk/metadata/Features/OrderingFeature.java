package com.hpe.adm.nga.sdk.metadata.Features;

/**
 * Created by ngthien on 8/3/2016.
 */
public class OrderingFeature extends Feature {

    private Aspect[] aspects;

    /**
     * get aspects
     * @return the ordering aspects
     */
    public Aspect[] getAspects() { return aspects; }

    public class Aspect {
        private String aspectName;
        private String orderFieldName;
        private String contextFieldName;

        public String getAspectName() { return aspectName; }
        public String getOrderFieldName() { return orderFieldName; }
        public String getContextFieldName() { return contextFieldName; }
    }
}
