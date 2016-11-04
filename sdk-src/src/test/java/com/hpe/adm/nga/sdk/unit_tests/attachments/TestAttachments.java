package com.hpe.adm.nga.sdk.unit_tests.attachments;

import static org.junit.Assert.*;

import com.hpe.adm.nga.sdk.network.HttpRequestFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.hpe.adm.nga.sdk.EntityListService;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.attachments.AttachmentList;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestAttachments {
	private static NGA nga;
	private static EntityListService service;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		nga = new NGA(CommonMethods.getRequestfactory(), CommonMethods.getDomain(), CommonMethods.getSharedSpace() , CommonMethods.getWorkSpace());
		AttachmentList attachments = nga.AttachmentList();
		AttachmentList spiedAttachments = PowerMockito.spy(attachments);
		service = (EntityListService)Whitebox.getInternalState(spiedAttachments, "entityListService");
	}
	
	@Test
	public void testCorrectUrl(){
		String expectedResult =  CommonMethods.getDomain() + "/api/shared_spaces/" + CommonMethods.getSharedSpace() + "/workspaces/" + CommonMethods.getWorkSpace() + "/attachments";
		String internalUrl = (String)Whitebox.getInternalState(service, "urlDomain");
		assertEquals(expectedResult, internalUrl);		
	}
}
