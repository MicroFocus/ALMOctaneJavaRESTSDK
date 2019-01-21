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
