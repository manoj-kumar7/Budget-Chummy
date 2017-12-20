package com.budgetchummy.api.util;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.PasswordUtil;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class LoginUtil {
	public static void loginWithBC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		boolean valid = false;

		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		long userid=-1;
		boolean verified = false;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		Connection con = null;
		PreparedStatement st=null;	
		ResultSet rs=null;	
		try {
			con = DriverManager.getConnection(url,user,mysql_password);
			st = con.prepareStatement("select password,user_id,verified from users where email=?;");
			st.setString(1, email);
			rs = st.executeQuery();
			String password=null;
			while(rs.next())
			{
				password = rs.getString("password");
				userid = rs.getLong("user_id");
				verified = rs.getBoolean("verified");
				// if(password.equals(pword))
				// {
				// 	valid = true;
				// }
				if(password == null || password == "")
				{
					valid = false;
				}
				else
				{
					valid = PasswordUtil.verifyPassword(pword, password);
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
			{
				try{
					rs.close();
				}catch (SQLException e) { /* ignored */}
			}
			try{
				st.close();
			}catch (SQLException e) { /* ignored */}
			try{
				con.close();
			}catch (SQLException e) { /* ignored */}
		}
		if(valid == true)
		{
			HttpSession session = request.getSession();
			session.setAttribute("user_id",userid);	
			if(verified == false)
			{	
				response.setStatus(403);	
			}
		}
		else
		{
			response.setStatus(401);
		}
	}

	public static void loginWithGoogle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		boolean emailVerified = false;
		boolean valid = false;
		String email = null;
		String idTokenString = request.getParameter("id_token");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		long userid=0;
		String CLIENT_ID = "1030027078466-lmvc5lqlkq1llqasuomhj2cnh6fv4at9.apps.googleusercontent.com";
		NetHttpTransport transport = new NetHttpTransport();
		JsonFactory mJFactory = new GsonFactory();
		String mProblem = "Verification failed. (Time-out?)";
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, mJFactory)
		    .setAudience(Collections.singletonList(CLIENT_ID))
		    .build();

		try{
			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken != null) {
			  Payload payload = idToken.getPayload();

			  // Print user identifier
			  String userId = payload.getSubject();
			  // System.out.println("User ID: " + userId);

			  // Get profile information from payload
			  email = payload.getEmail();
			  // System.out.println("Email: " + email);
			  emailVerified = Boolean.valueOf(payload.getEmailVerified());

			} 
			else 
			{
			  	response.setStatus(400);
			}			
		}
		catch(GeneralSecurityException e){
			mProblem = "Security issue: " + e.getLocalizedMessage();
		}

		if(emailVerified)
		{
			Connection con = null;
			PreparedStatement st=null;
			ResultSet rs=null;
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				st = con.prepareStatement("select user_id from users where email=? AND google_signin=?;");
				st.setString(1, email);
				st.setBoolean(2, true);
				rs = st.executeQuery();
				while(rs.next())
				{
					userid = rs.getLong("user_id");
					valid = true;
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null)
				{
					try{
						rs.close();
					}catch (SQLException e) { /* ignored */}
				}
				try{
					st.close();
				}catch (SQLException e) { /* ignored */}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
			if(valid)
			{
				HttpSession session = request.getSession();
				session.setAttribute("user_id",userid);	
			}
			else
			{
				response.setStatus(401);
			}
		}
	}

}
