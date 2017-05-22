/*
 *
 *
 *    Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package com.hpe.adm.nga.sdk.metadata.fieldfeatures;

/**
 *
 * Created by ngthien on 8/3/2016.
 */
class BusinessRules {
    private static final String name = "business_rules";
    private boolean show_in_action;
    private boolean show_in_condition;

    public String getName() {return name;}
    public boolean getShowInAction() {return show_in_action;}
    public boolean getShowInCondition() {return show_in_condition;}
}
