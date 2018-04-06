/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.model;

/**
 *
 * This class hold the LongFieldModel objects and serve as a long type FieldModel data holder 
 *
 *
 */
public class LongFieldModel implements FieldModel<Long> {
	
	//Private 
		private String name = "";
		private Long value = 0L;
		
		/**
		 * Creates a new LongFieldModel object
		 * 
		 * @param newName - Field name
		 * @param newValue - Field Value
		 */
		public LongFieldModel(String newName,Long newValue){
			
			setValue(newName,newValue);
		}
		
		/**
		 * get value
		 */
		public Long getValue()	{
			return value;
		}

    /**
		 * get Field's name
		 */
		public String getName(){
			return name;
		}
		
		/**
		 * set Field's name/value
		 */
		public void setValue(String newName,Long newValue){
			
			name = newName;
			value = newValue;
		}

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LongFieldModel other = (LongFieldModel) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

}