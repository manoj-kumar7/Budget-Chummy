package com.budgetchummy.api.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;
import com.budgetchummy.api.util.PasswordUtil;
import com.budgetchummy.api.util.emailUtil;
import com.budgetchummy.api.util.messageDigestUtil;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.*;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;


public class SignUpUtil {
	public static void signUpWithBC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		long added_date = Long.parseLong(request.getParameter("created_date_time"));
		long userid=0;
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		
		String generatedPassword = PasswordUtil.generatePassword(pword);

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null;
			ResultSet rs = null;
			String query=null;

		    // DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    // Date dateobj = new Date();
		    // df.setTimeZone(TimeZone.getTimeZone("IST"));
		    // added_date = Datehelper.dateToEpoch(df.format(dateobj));
		    st = con.prepareStatement("select * from users where email=?");
		    st.setString(1, email);
		    rs = st.executeQuery();
		    if(rs.next())
			{
				response.setStatus(401);
			}
			else
			{
				boolean activationNeeded = false;
				if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
				{
					activationNeeded = true;
				}
				String activation_code = messageDigestUtil.getMD5Hash(email);
				st = con.prepareStatement("insert into users(first_name,last_name,email,password,created_date_time,verified,activation_code) values(?,?,?,?,?,?,?);");
				st.setString(1, first_name);
				st.setString(2, last_name);
				st.setString(3, email);
				st.setString(4, generatedPassword);
				st.setLong(5, added_date);
				st.setBoolean(6, !activationNeeded);
				st.setString(7, activation_code);
				int i = st.executeUpdate();

				if(activationNeeded)
				{
					String rootURL = APIConstants.rootURL;
					String finalURL = rootURL + "activate?code=%27"+activation_code+"%27&email=%27"+email+"%27";
					String subject = "Activate your Budget Chummy account";
					String email_welcome_html = "<div class='email-welcome-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>Hello " + first_name + "!</div>";
					String email_content_html = "<div class='email-content-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>Before you set your first budget, please take a moment to verify your email address</div>";
					String email_button_html = "<div style='margin: 0 auto;width: 300px;margin-top: 30px;margin-bottom: 30px;'><a href='"+finalURL+"' id='verify-email-btn' class='email-btn' style='display: inline-block;background-color: #f4511e;border: none;color: #FFFFFF;text-align: center;font-size: 17px;padding: 12px;width: 300px;font-family: \"Trebuchet MS\";letter-spacing: 1px;text-decoration: none;border-radius: 3px;cursor: pointer;'>VERIFY YOUR EMAIL ADDRESS</a></div>";

					String message = "<div class='email-box' style='width:70%;height:100%;background-color:#e2e2e2;margin:0 auto;'>"+email_welcome_html+
									 email_content_html+
									 email_button_html+ "</div>";
					emailUtil.sendMail(email, subject, message);
				}
				rs = null;
				st = con.prepareStatement("select user_id from users where email=?;");
				st.setString(1, email);
				rs = st.executeQuery();

				while(rs.next())
				{
					userid = rs.getInt("user_id");
				}
				HttpSession session = request.getSession();
				session.setAttribute("user_id",userid);

			}

			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void signUpWithGoogle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String name = null, email = null;
		boolean emailVerified = false;
		String idTokenString = request.getParameter("id_token");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		long added_date = Long.parseLong(request.getParameter("created_date_time"));
		long userid=0;
		String CLIENT_ID = "1030027078466-lmvc5lqlkq1llqasuomhj2cnh6fv4at9.apps.googleusercontent.com";
		NetHttpTransport transport = new NetHttpTransport();
		JsonFactory mJFactory = new GsonFactory();
		
		String mProblem = "Verification failed. (Time-out?)";

		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		
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
			  name = (String) payload.get("name");
			  // System.out.println("Name: " + name);
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
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			
			try {
				Connection con = null;
				con = DriverManager.getConnection(url,user,mysql_password);
				PreparedStatement st=null;
				ResultSet rs = null;
				String query=null;

			    // DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			    // Date dateobj = new Date();
			    // df.setTimeZone(TimeZone.getTimeZone("IST"));
			    // added_date = Datehelper.dateToEpoch(df.format(dateobj));
			    st = con.prepareStatement("select * from users where email=?");
			    st.setString(1, email);
			    rs = st.executeQuery();
			    if(rs.next())
				{
					response.setStatus(401);
				}
				else
				{
					String activation_code = messageDigestUtil.getMD5Hash(email);
					st = con.prepareStatement("insert into users(first_name,email,created_date_time,verified) values(?,?,?,?);");
					st.setString(1, name);
					st.setString(2, email);
					st.setLong(3, added_date);
					st.setBoolean(4, true);
					int i = st.executeUpdate();

					rs = null;
					st = con.prepareStatement("select user_id from users where email=?;");
					st.setString(1, email);
					rs = st.executeQuery();

					while(rs.next())
					{
						userid = rs.getInt("user_id");
					}
					HttpSession session = request.getSession();
					session.setAttribute("user_id",userid);

				}

				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			response.setStatus(9001);
		}
	}
}
