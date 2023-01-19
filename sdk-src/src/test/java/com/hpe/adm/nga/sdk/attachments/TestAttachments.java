/*
 * © Copyright 2016-2023 Micro Focus or one of its affiliates.
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
