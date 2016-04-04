package java.com.hpe.adm.nga.sdk;

import java.com.hpe.adm.nga.sdk.Entity.EntityList.Create;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Delete;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Get;
import java.com.hpe.adm.nga.sdk.Entity.EntityList.Update;

/**
 * Created by brucesp on 23/02/2016.
 */
public class Query {


	/*public Query parameter (String name, Object value){
		return this;
	}*/
	
	public Field field(String Name) {
		return new Field();
	}
	
	public static class Field {
		public Logical equeal(Object value) {
			return new Logical();
		}

		public Logical notEqueal(Object value) {
			return new Logical();
		}

		public Logical less(Object value) {
			return new Logical();
		}

		public Logical greater(Object value) {
			return new Logical();
		}
		
		public Logical lessEqueal(Object value) {
			return new Logical();
		}
		
		public Logical greaterEqueal(Object value) {
			return new Logical();
		}
		
		public static class Logical {
			
			public Query or() {
				return new Query();
			}
			
			public Query and() {
				return new Query();
			}
			
			public Query build() {
				return new Query();
			}
		}
	}
	
	

}
