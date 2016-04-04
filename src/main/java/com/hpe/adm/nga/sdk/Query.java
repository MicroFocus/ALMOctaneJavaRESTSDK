package com.hpe.adm.nga.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;

/**
 * Created by brucesp on 23/02/2016.
 */
public class Query {

	public static String strQuery = "";
	//private String strlogical;
	//private String strValue;
	
	public Field field (String name){
		return new Field(name,this);
	}
	
	public String getQueryString() {
		
		return strQuery;
	}

	protected void setQuery(String quary) {
		
		strQuery = quary;
	}
	
	protected void addQuery(String add) {
		
		strQuery += add;
	}
	
	public static class Field {
		
		private String strName = "";
		private Query querybase = null;
		
		public Field(String Name,Query base) {
			strName = Name;
			querybase = base;
		}
		
		public Field(String Name) {
			strName = Name;
			querybase = null;
		}
		
		public Logical equal(Object value) {
			return new Logical(querybase,strName,"EQ",value);
		}

		public Logical less(Object value) {
			return new Logical(querybase,strName,"LT",value);
		}

		public Logical greater(Object value) {
			return new Logical(querybase,strName,"GT",value);
		}
		
		public Logical lessEqual(Object value) {
			return new Logical(querybase,strName,"LE",value);
		}
		
		public Logical greaterEqual(Object value) {
			return new Logical(querybase,strName,"GE",value);
		}
		
		public static class Logical {
			
			private String strName;
			private Object objValue;
			private String strSign;
			private Query queryBase;
			
			public Logical(Query base,String name,String sign,Object value){
				
				strName = name;
				objValue = value;
				strSign = sign;
				queryBase = base;
			}
			
			public Query or() {
				
				Query quary = build();
				quary.addQuery("||");
				return quary;
			}
			
			public Query and() {
				
				Query quary = build();
				quary.addQuery(",");
				return quary;
			}
			
			public Query build() {
				
				String strbase = "";
				String strValue = objValue.toString();
				
				if(queryBase!=null)
					strbase = queryBase.getQueryString();
				
				if (objValue.getClass() == Date.class)
				{
					String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
					SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
					TimeZone utc = TimeZone.getTimeZone("UTC");
					sdf.setTimeZone(utc);
					strValue = "'" + sdf.format(objValue) + "'";
				}
				else if (objValue.getClass() == String.class)
				{
					strValue = "'" + strValue + "'";
				}
									
				
				String  strQuery = strbase +  strName + " " + strSign + " " + strValue;
				Query quary = new Query();
				quary.setQuery(strQuery);
				return quary;
			}
		}
	}
	
	

}
