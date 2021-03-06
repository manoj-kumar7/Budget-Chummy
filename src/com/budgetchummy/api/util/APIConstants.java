package com.budgetchummy.api.util;

public interface APIConstants {

//		public static final String POSTGRESQL_URL = "jdbc:postgresql://ec2-23-21-220-32.compute-1.amazonaws.com:5432/d94ho6f7ohbjff";
//		public static final String POSTGRESQL_USERNAME = "avuwdoyswatqss";
//		public static final String POSTGRESQL_PASSWORD = "6f02a2ac931b0b461240517be38df571add53ee70d6424922ab03e27d488936a";
//		public static final String rootURL = "https://budgetchummy.herokuapp.com/";

		public static final String POSTGRESQL_HOST = System.getProperty("RDS_HOST");
		public static final String POSTGRESQL_DB = System.getProperty("RDS_DB");
		public static final String POSTGRESQL_PORT = System.getProperty("RDS_PORT");
		public static final String POSTGRESQL_URL = "jdbc:postgresql://"+ POSTGRESQL_HOST + ":" + POSTGRESQL_PORT + "/" + POSTGRESQL_DB;
		public static final String POSTGRESQL_USERNAME = System.getProperty("RDS_USERNAME");
		public static final String POSTGRESQL_PASSWORD = System.getProperty("RDS_PASSWORD");
		public static final String EMAIL = System.getProperty("BUSINESS_EMAIL");
		public static final String EMAIL_PASSWORD = System.getProperty("BUSINESS_EMAIL_PASSWORD");
		public static final String rootURL = "http://budgetchummy.com/";
	
//		public static final String POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/budgetchummy";
//		public static final String POSTGRESQL_USERNAME = "manojbc";
//		public static final String POSTGRESQL_PASSWORD = "manojbc";
//		public static final String EMAIL = "manoj@budgetchummy.com";
//		public static final String EMAIL_PASSWORD = "Manoj@BC1";
//		public static final String rootURL = "http://localhost:8080/BudgetChummy/";
}
