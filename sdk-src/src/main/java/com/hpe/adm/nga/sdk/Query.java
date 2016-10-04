package com.hpe.adm.nga.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class serve all functionality concern to query ( filtering)
 * Each Query is build based on the following scheme : {Field} {Logic procedure} {value}
 * @author Moris Oz
 *
 */
public class Query {
	
	private String query = "";

	public Query() {
	}

	/**
	 * getter of field object 
	 * @param name - field name
	 * @return - field object
	 */
	public Field field (String name,boolean negate) {
		return new Field(name, this, negate);
	}
	
	/**
	 * getter of field object 
	 * @param name - field name
	 * @return - field object
	 */
	public Field field (String name){
		return new Field(name, this, false);
	}
	
	/**
	 * getter of a query string
	 * @return query string
	 */
	public String getQueryString() {
		
		return query;
	}
	
	/**
	 * setter of new query string
	 * @param qry - query string
	 */
	protected void setQuery(String qry) {
		
		query = qry;
	}
	
	/**
	 * add new query
	 * @param qry - query string
	 */
	protected void addQuery(String add) {
		
		query += add;
	}
	
	/**
	 * This class hold the query's field data and functionality
	 * @author mor4095
	 *
	 */
	public static class Field {
		
		// constant
		private static final String COMPARISON_OPERATOR_EQUALS 			= "EQ";
		private static final String COMPARISON_OPERATOR_LESS			= "LT";
		private static final String COMPARISON_OPERATOR_GREATER 		= "GT";
		private static final String COMPARISON_OPERATOR_LESS_EQUALS  	= "LE";
		private static final String COMPARISON_OPERATOR_GREATER_EQUALS 	= "GE";
		
		
		// private
		private String queryName = "";
		private Query queryBase = null;
		private boolean isNegate = false;
		
		// functions
		
		/**
		 * Creates a new Field object based on a given name and base query ( A filed that is already part of a given query )
		 * @param Name - Field name
		 * @param base - query base
		 */
		protected Field(String Name, Query base, boolean negate) {
			queryName = Name;
			queryBase = base;
			isNegate = negate;
		}
		
		/**
		 * Creates a new Field object on a given name ( new query )
		 * @param Name - field name
		 */
		public Field(String Name) {
			queryName = Name;
			queryBase = null;
			isNegate = false;
		}
		
		/**
		 * Creates a new Field object with negate value
		 * @param Name - field name
		 * @param negate - (Eg: field equal value , field not equal value )
		 */
		public Field(String Name,boolean negate) {
			queryName = Name;
			queryBase = null;
			isNegate = negate;
		}

		/**
		 * getter of an equal logical Object based on a given value ( Support any given value type )
		 * @param value
		 * @return - a new logical Object based on a given value
		 */
		public Field not() {
			return new Field(queryName, queryBase, !isNegate);
			//isNegate = true;
		}

		/**
		 * getter of an equal logical Object based on a given value ( Support any given value type )
		 * @param value 
		 * @return - a new logical Object based on a given value
		 */
		public Logical equalTo(Object value) {
			return new Logical(queryBase,queryName,COMPARISON_OPERATOR_EQUALS,value,isNegate);
		}
		
		/**
		 * getter of an less logical Object based on a given value ( Support any given value type )
		 * @param value 
		 * @return - a new logical Object based on a given value
		 */
		public Logical lessThan(Object value) {
			return new Logical(queryBase,queryName,COMPARISON_OPERATOR_LESS,value,isNegate);
		}
		
		/**
		 * getter of an greater logical Object based on a given value ( Support any given value type )
		 * @param value 
		 * @return - a new logical Object based on a given value
		 */
		public Logical greaterThan(Object value) {
			return new Logical(queryBase,queryName,COMPARISON_OPERATOR_GREATER,value,isNegate);
		}
		
		/**
		 * getter of an lessEqual logical Object based on a given value ( Support any given value type )
		 * @param value 
		 * @return - a new logical Object based on a given value
		 */
		public Logical lessOrEqualThan(Object value) {
			return new Logical(queryBase,queryName,COMPARISON_OPERATOR_LESS_EQUALS,value,isNegate);
		}
		
		/**
		 * getter of an greaterEqual logical Object based on a given value ( Support any given value type )
		 * @param value 
		 * @return - a new logical Object based on a given value
		 */
		public Logical greaterOrEqualThan(Object value) {
			return new Logical(queryBase,queryName,COMPARISON_OPERATOR_GREATER_EQUALS,value,isNegate);
		}
		
		/**
		 * This class hold the query's Logical procedure functionality
		 * @author moris oz
		 *
		 */
		public static class Logical {
			
			// constant
			private  static final String DATE_TIME_ISO_FORMAT 	= "yyyy-MM-dd'T'HH:mm:ss'Z'";
			private  static final String DATE_TIME_UTC_ZONE_NAME 	= "UTC";
			
			// private members
			private String nameLogical="";
			private Object objValue=null;
			private String signLogical="";
			private Query queryBase=null;
			private boolean isNegate=false;
			
			// functions
			
			/**
			 * Creates a new Logical object 
			 * @param base - base Query ( query can contain dew Logical statements )
			 * @param sign - query's logical sign
			 * @param sign - query's value
			 * @param negate - is negate ?
			 */
			public Logical(Query base,String name,String sign,Object value,boolean negate) {
				nameLogical = name;
				objValue = value;
				signLogical = sign;
				queryBase = base;
				isNegate = negate; 
			}

			public Logical(Query base, String name, boolean negate) {
				nameLogical = name;
				queryBase = base;
				isNegate = negate;
			}

			/**
			 * getter of a new query with "Or" procedure
			 * @return
			 */
			public Query or() {
				
				Query quary = build();
				quary.addQuery("||");
				return quary;
			}
			
			/**
			 * getter of a new query with "And" procedure
			 * @return
			 */
			public Query and() {
				
				Query quary = build();
				quary.addQuery(";");
				return quary;
			}
			
			/**
			 * Build new query
			 * @return
			 */
			public Query build() {
				String base = "";
				String value = "";
				String isNegatebuild = "";
				
				if (isNegate)
					isNegatebuild = "!";
				
				if(queryBase != null)
					base = queryBase.getQueryString();
				
				if( objValue == null)
				{
					value = "{null}";
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
