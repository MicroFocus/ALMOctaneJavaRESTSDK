package com.hpe.adm.nga.sdk.attachments;

import com.hpe.adm.nga.sdk.Octane;
import com.hpe.adm.nga.sdk.unit_tests.common.CommonMethods;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TestAttachments {
	private static Octane octane;
	private static AttachmentList spiedAttachments;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		octane = CommonMethods.getOctaneForTest();
		AttachmentList attachments = octane.attachmentList();
		spiedAttachments = PowerMockito.spy(attachments);
	}
	
	@Test
	public void testCorrectUrl(){
		String expectedResult =  CommonMethods.getDomain() + "/api/shared_spaces/" + CommonMethods.getSharedSpace() + "/workspaces/" + CommonMethods.getWorkSpace() + "/attachments";
		String internalUrl = (String)Whitebox.getInternalState(spiedAttachments, "attachmentListDomain");
		assertEquals(expectedResult, internalUrl);		
	}
}
