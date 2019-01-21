/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * This class holds the DateFieldModel objects and serves as a ZonedDateTime type FieldModel data holder.  Note according to
 * the Octane server specification all dates are stored in the UTC time zone.  Therefore when sending information to the
 * server the timezone is converted to UTC (whilst keeping the same instant).  All dates that are returned are in the UTC
 * time zone and it is up to the consumer to convert to the correct time zone if necessary
 *
 *
 */
public class DateFieldModel implements FieldModel<ZonedDateTime> {
	
	//Private 
		private String name = "";
		private ZonedDateTime value = null;
		
		/**
		 * Creates a new DateFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public DateFieldModel(String newName,ZonedDateTime newValue){
			
			setValue(newName,newValue);
		}
		
		/**
		 * get the date in the UTC time zone
		 */
		public ZonedDateTime getValue()	{
			return value;
		}

	/**
		 * get Field's name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * set Field's name/value.  The timezone will be converted to UTC upon being sent to the server
		 */
		public void setValue(String newName,ZonedDateTime newValue){
			
			name = newName;
			value = newValue.withZoneSameInstant(ZoneId.of("Z"));
		}


}
