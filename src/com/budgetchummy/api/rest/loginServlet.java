package com.budgetchummy.api.rest;


import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.budgetchummy.api.util.APIConstants;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
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
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null;
			st = con.createStatement();
			String query = "select password,user_id from users where email='"+email+"';";
			ResultSet rs=null;
			rs = st.executeQuery(query);
			String password=null;
			while(rs.next())
			{
				password = rs.getString("password");
				userid = rs.getLong("user_id");
			}
			if(password.equals(pword))
			{
				valid = true;
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
			session.setAttribute("useremail",email);
			session.setAttribute("user_id",userid);
			if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
			{
				String homeurl = new String("ChooseAccount.jsp");
				response.setStatus(response.SC_MOVED_TEMPORARILY);
		        response.setHeader("Location", homeurl);				
			}
			else
			{
				String homeurl = new String("AccountAuthentication.jsp?account_id="+account_id+"&invitation_id="+invitation_id);
				response.setStatus(response.SC_MOVED_TEMPORARILY);
		        response.setHeader("Location", homeurl);			
		    }

		}
		else
		{
			if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
			{
				String backurl = new String("login.jsp?account_id="+account_id+"&invitation_id="+invitation_id);
				response.setStatus(response.SC_MOVED_TEMPORARILY);
		        response.setHeader("Location", backurl);
			}
			else
			{
				String backurl = new String("login.jsp");
				response.setStatus(response.SC_MOVED_TEMPORARILY);
		        response.setHeader("Location", backurl);
			}
		}
		
	}

}
