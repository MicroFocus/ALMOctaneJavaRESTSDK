package com.hpe.adm.nga.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by brucesp on 23/02/2016.
 */
public class Query {

	
	//public members
	private  String query = "";
	
	
	public Field field (String name){
		return new Field(name,this);
	}
	
	public String getQueryString() {
		
		return query;
	}

	protected void setQuery(String qry) {
		
		query = qry;
	}
	
	protected void addQuery(String add) {
		
		query += add;
	}
	
	public static class Field {
		
		// constant
		private final String COMPARISON_OPERATOR_EQUALS 			= "EQ";
		private final String COMPARISON_OPERATOR_LESS			= "LT";
		private final String COMPARISON_OPERATOR_GREATER 		= "GT";
		private final String COMPARISON_OPERATOR_LESS_EQUALS  	= "LE";
		private final String COMPARISON_OPERATOR_GREATER_EQUALS 	= "GE";
		
		
		// private
		private String queryName = "";
		private Query querybase = null;
		private boolean isNegate = false;
		
		// functions
		public Field(String Name,Query base) {
			queryName = Name;
			querybase = base;
			isNegate = false;
		}
		
		public Field(String Name) {
			queryName = Name;
			querybase = null;
			isNegate = false;
		}
		
		public Field(String Name,boolean negate) {
			queryName = Name;
			querybase = null;
			isNegate = negate;
		}
		
		
		public Logical equal(Object value) {
			return new Logical(querybase,queryName,COMPARISON_OPERATOR_EQUALS,value,isNegate);
		}

		public Logical less(Object value) {
			return new Logical(querybase,queryName,COMPARISON_OPERATOR_LESS,value,isNegate);
		}

		public Logical greater(Object value) {
			return new Logical(querybase,queryName,COMPARISON_OPERATOR_GREATER,value,isNegate);
		}
		
		public Logical lessEqual(Object value) {
			return new Logical(querybase,queryName,COMPARISON_OPERATOR_LESS_EQUALS,value,isNegate);
		}
		
		public Logical greaterEqual(Object value) {
			return new Logical(querybase,queryName,COMPARISON_OPERATOR_GREATER_EQUALS,value,isNegate);
		}
		
		public static class Logical {
			
			// constant
			private  final String DATE_TIME_ISO_FORMAT 	= "yyyy-MM-dd'T'HH:mm:ss'Z'";
			private  final String DATE_TIME_UTC_ZONE_NAME 	= "UTC";
			
			// private members
			private String nameLogical="";
			private Object objValue=null;
			private String signLogical="";
			private Query queryBase=null;
			private boolean isNegate=false;
			
			// functions
			public Logical(Query base,String name,String sign,Object value,boolean negate){
				
				nameLogical = name;
				objValue = value;
				signLogical = sign;
				queryBase = base;
				isNegate = negate; 
			}
			
			public Query or() {
				
				Query quary = build();
				quary.addQuery("||");
				return quary;
			}
			
			public Query and() {
				
				Query quary = build();
				quary.addQuery(";");
				return quary;
			}
			
			public Query build() {
				
				String base = "";
				String value = "";
				String isNegatebuild = "";
				
				if (isNegate)
					isNegatebuild = "!";
				
				if(queryBase!=null)
					base = queryBase.getQueryString();
				
				if( objValue == null)
				{
					value = "null";
				}
				else if (objValue.getClass() == Date.class)
				{
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_ISO_FORMAT);
					TimeZone utc = TimeZone.getTimeZone(DATE_TIME_UTC_ZONE_NAME);
					sdf.setTimeZone(utc);
					value = "'" + sdf.format(objValue) + "'";
				}
				else if (objValue.getClass() == String.class)
				{
					value = "'" +  objValue.toString() + "'";
				}
				else if (objValue.getClass() == Query.class) 
				{
					value =  "{" + ((Query)objValue).getQueryString() +  "}" ;
				}
				else
				{
					value = objValue.toString();
				}
				
				String  strQuery = base +  isNegatebuild + "(" + nameLogical + " " + signLogical + " " + value + ")";
				Query quary = new Query();
				quary.setQuery(strQuery);
				return quary;
			}
		}
	}
	
	

}
