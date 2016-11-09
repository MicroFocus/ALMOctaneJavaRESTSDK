package com.hpe.adm.nga.sdk.metadata.features;

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
        private String aspect_name;
        private String order_field_name;
        private String context_field_name;

        public String getAspectName() { return aspect_name; }
        public String getOrderFieldName() { return order_field_name; }
        public String getContextFieldName() { return context_field_name; }
    }
}
