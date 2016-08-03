package com.hpe.adm.nga.sdk.metadata.fieldfeatures;

/**
 * Created by ngthien on 8/3/2016.
 */
public class BusinessRules {
    private final String name = "business_rules";
    private boolean show_in_action;
    private boolean show_in_condition;

    public String getName() {return name;}
    public boolean getShowInAction() {return show_in_action;}
    public boolean getShowInCondition() {return show_in_condition;}
}
