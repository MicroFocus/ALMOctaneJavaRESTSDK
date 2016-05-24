package com.hpe.adm.nga.sdk.base;

import com.hpe.adm.nga.sdk.EntityList;
import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.authorisation.UserAuthorisation;
import com.hpe.adm.nga.sdk.metadata.Metadata;
import com.hpe.adm.nga.sdk.utils.HttpUtils;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Created by Guy Guetta on 12/04/2016.
 */
public class TestBase {

	final static String MY_APP_ID = "moris@korentec.co.il";
	final static String MY_APP_SECRET = "Moris4095";

	protected static NGA nga;
	protected static String entityName = "";
	private static String entityTypeOld = "";
	protected static EntityList entityList;
	protected static Metadata metadata;

	static {
		// for local execution
		if (System.getProperty("should.set.proxy") == null) {
			System.setProperty("should.set.proxy", "true");
		}
	}

	@BeforeClass
	public static void init() {
		HttpUtils.SetSystemKeepAlive(false);
		HttpUtils.SetSystemProxy();

		nga = new NGA.Builder(
				new UserAuthorisation(MY_APP_ID, MY_APP_SECRET)).Server("https://mqast001pngx.saas.hpe.com").sharedSpace(4063).workSpace(1002).build();
		metadata = nga.metadata();
	}

	@Before
	public void before() {
		if (!entityName.equals(entityTypeOld)) {
			entityList = nga.entityList(entityName);
			entityTypeOld = entityName;
		}
	}

}
