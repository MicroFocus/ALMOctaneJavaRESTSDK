package java.com.hpe.adm.nga.sdk.attachments;


import java.com.hpe.adm.nga.sdk.EntityList;
import java.util.Collection;

/**
 * Created by Moris on 23/02/2016.
 */
public class Attachments extends EntityList{

	
	public Entities at(int entityId) {
		return null;
	}
	
	public Update update() {
		return null;
	}
	
	public class Entities extends EntityList.Entities {
		
		// Get Binary Data
		public Get getBinary() {
			return null;
		}
		
		/// Not supported
		public Update update() {
			return null;
		}
	}
}
