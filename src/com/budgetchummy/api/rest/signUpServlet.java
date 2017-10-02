package com.budgetchummy.api.rest;


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
/**
 * Servlet implementation class signUpServlet
 */
@WebServlet(urlPatterns = {"/api/v1/signUp", "/BudgetChummy/api/v1/signUp"})
public class signUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public signUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		long userid=0,added_date=0;
		
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

		    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    Date dateobj = new Date();
		    df.setTimeZone(TimeZone.getTimeZone("IST"));
		    added_date = Datehelper.dateToEpoch(df.format(dateobj));
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
				st = con.prepareStatement("insert into users(first_name,last_name,email,password,created_date_time,verified,activation_code) values(?,?,?,?,?,?,?);");
				st.setString(1, first_name);
				st.setString(2, last_name);
				st.setString(3, email);
				st.setString(4, generatedPassword);
				st.setLong(5, added_date);
				st.setBoolean(6, false);
				st.setString(7, activation_code);
				int i = st.executeUpdate();

				String rootURL = APIConstants.rootURL;
				String subject = "Activate your Budget Chummy account";
				String message = "Hi " + first_name + "\n Before you set your first budget, please take a moment to verify your email address \n"+
								 rootURL+"activate?code=%27"+activation_code+"%27&email=%27"+email+"%27";
				emailUtil.sendMail(email, subject, message);
				// rs = null;
				// st = con.prepareStatement("select user_id from users where email=?;");
				// st.setString(1, email);
				// rs = st.executeQuery();

				// while(rs.next())
				// {
				// 	userid = rs.getInt("user_id");
				// }
				// HttpSession session = request.getSession();
				// session.setAttribute("user_id",userid);

			}

			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	

		
//		if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
//		{
//			String homeurl = new String("CreateAccount");
//			response.setStatus(response.SC_MOVED_TEMPORARILY);
//	        response.setHeader("Location", homeurl);				
//		}
//		else
//		{
//			String homeurl = new String("AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id);
//			response.setStatus(response.SC_MOVED_TEMPORARILY);
//	        response.setHeader("Location", homeurl);
//		}

	}

}
