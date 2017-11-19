package com.budgetchummy.api.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

import com.budgetchummy.api.util.APIConstants;

public class JobsUtil {
	public static void deleteJob(long transaction_id) throws IOException{
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null;
			st = con.prepareStatement("delete from jobs where data_id=?");
			st.setLong(1, transaction_id);		
			int i = st.executeUpdate();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
