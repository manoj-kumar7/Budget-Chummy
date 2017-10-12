package com.budgetchummy.api.rest;


import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.PasswordUtil;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet(urlPatterns = {"/api/v1/login", "/BudgetChummy/api/v1/login"})
public class loginServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    			
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
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
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null;
			st = con.prepareStatement("select password,user_id,verified from users where email=?;");
			st.setString(1, email);
			ResultSet rs=null;
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
				valid = PasswordUtil.verifyPassword(pword, password);
			}	
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(valid == true)
		{
			HttpSession session = request.getSession();
			session.setAttribute("user_id",userid);	
			if(verified == false)
			{	
				response.setStatus(403);	
			}
//			if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
//			{
//				String homeurl = new String("ChooseAccount");
//				response.setStatus(response.SC_MOVED_TEMPORARILY);
//		        response.setHeader("Location", homeurl);				
//			}
//			else
//			{
//				String homeurl = new String("AccountAuthentication?account_id="+account_id+"&invitation_id="+invitation_id);
//				response.setStatus(response.SC_MOVED_TEMPORARILY);
//		        response.setHeader("Location", homeurl);			
//		    }

		}
		else
		{
			//String backurl = new String("login");
			response.setStatus(401);
	        //response.setHeader("Location", backurl);
		}
		
	}

}
