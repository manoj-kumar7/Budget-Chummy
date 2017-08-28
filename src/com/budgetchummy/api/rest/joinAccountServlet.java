package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.budgetchummy.api.util.APIConstants;


@WebServlet(urlPatterns = {"/joinAccount", "/BudgetChummy/joinAccount"})
public class joinAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public joinAccountServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Object user_attribute = session.getAttribute("user_id");
		long userid = Long.parseLong(String.valueOf(user_attribute));
		
		String passcode = request.getParameter("passcode");
		long account_id = Long.parseLong(request.getParameter("account_id"));
		long invitation_id = Long.parseLong(request.getParameter("invitation_id"));
		String passcode_from_db = null,invitation_status=null;
		String query = null;
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null,st1=null;
			st = con.createStatement();
			st1 = con.createStatement();
			ResultSet rs=null;
			query = "select passcode,invitation_status from invitations where invitation_id="+invitation_id+";";
			rs = st.executeQuery(query);
			while(rs.next())
			{
				passcode_from_db = rs.getString("passcode");
				invitation_status = rs.getString("invitation_status");
			}
			if(invitation_status.equals("joined"))
			{
				
			}
			else if(passcode.equals(passcode_from_db))
			{
				query = "insert into adduser(account_id,user_id,role) values("+account_id+","+userid+",'"+"user"+"');";
				st1.executeUpdate(query);
				query = "update accounts set no_of_members=no_of_members+1 where account_id="+account_id+";";
				st1.executeUpdate(query);
				query = "update invitations set invitation_status='joined' where invitation_id="+invitation_id+";";
				st1.executeUpdate(query);
				session.setAttribute("account_id",account_id);
			}
			rs.close();
			st.close();
			st1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(passcode.equals(passcode_from_db))
		{
			String homeurl = new String("home.jsp");
			response.setStatus(response.SC_MOVED_TEMPORARILY);
	        response.setHeader("Location", homeurl);
		}
	}

}
