/*
 * Copyright 2016-2025 Open Text.
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.exception;

import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class TestOctaneExceptions {

	private static FieldModel stringField;
	private static FieldModel booleanField;
	private static FieldModel longField;
	private static Set<FieldModel> set;
	
	@BeforeClass
	public static void beforeClass(){
		stringField = new StringFieldModel("stringField", "value");
		booleanField = new BooleanFieldModel("booleanField", true);
		longField = new LongFieldModel("longField", new Long(5));
		set = new HashSet<FieldModel>();
		set.add(stringField);
		set.add(booleanField);
		set.add(longField);
	}
	
	@Test 
	public void testOctaneException(){
		ErrorModel err = new ErrorModel(set);
		OctaneException exc = new OctaneException(err);
		
		// get the internal errorSet and check equivalence
		ErrorModel internalError = exc.getError();
		assertTrue(checkEquivalence(internalError.getValues()));		
	}
	
	@Test 
	public void testOctanePartialException(){
		Collection<ErrorModel> errorCol = new ArrayList<ErrorModel>();
		Collection<EntityModel> entityCol = new ArrayList<EntityModel>();
		errorCol.add(new ErrorModel(set));
		errorCol.add(new ErrorModel(set));
		entityCol.add(new EntityModel(set));
		entityCol.add(new EntityModel(set));
		
		OctanePartialException exc = new OctanePartialException(errorCol, entityCol);
		// get the internal fields state and check for equivalence
		Collection<EntityModel> internalEntities = exc.getEntitiesModels();
		Collection<ErrorModel> internalErrors = exc.getErrorModels();
		assertTrue(CommonUtils.isCollectionAInCollectionB(internalEntities, entityCol));
		assertTrue(CommonMethods.isErrorCollectionAInErrorCollectionB(internalErrors, errorCol));
		
	}
	
	private boolean checkEquivalence(Set<FieldModel> fields){
		boolean ret = true;
		for (FieldModel field : fields){
			if (field instanceof StringFieldModel){
				ret &= field.getValue().equals(stringField.getValue()) &&
						   field.getName().equals(stringField.getName());
			} else if (field instanceof BooleanFieldModel){
				ret &= field.getValue().equals(booleanField.getValue()) &&
						   field.getName().equals(booleanField.getName());
			} else if (field instanceof LongFieldModel){
				ret &= field.getValue().equals(longField.getValue()) &&
						   field.getName().equals(longField.getName());
			}else {
				fail("got an unrecognized fieldModel type");
			}
		}
		
		return ret;
	}
}
