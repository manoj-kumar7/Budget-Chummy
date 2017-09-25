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


@WebServlet(urlPatterns = {"/api/v1/joinAccount", "/BudgetChummy/api/v1/joinAccount"})
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
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(400);
		}
		else
		{
			Object user_attribute = session.getAttribute("user_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			String passcode = request.getParameter("passcode");
			long account_id = Long.parseLong(request.getParameter("account_id"));
			long invitation_id = Long.parseLong(request.getParameter("invitation_id"));
			String passcode_from_db = null,invitation_status=null;
			String query = null;
			boolean isValidURL = false;
			String logged_in_mail_id = null;

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
				PreparedStatement st=null,st1=null;
				ResultSet rs=null;
				st = con.prepareStatement("select email from users where user_id=?;");
				st.setLong(1, userid);
				rs = st.executeQuery();
				if(rs.next())
				{
					logged_in_mail_id = rs.getString("email");
				}
				rs = null;

				st = con.prepareStatement("select passcode,invitation_status from invitations where invitation_id=? AND for_account=? AND sent_to=?");
				st.setLong(1, invitation_id);
				st.setLong(2, account_id);
				st.setString(3, logged_in_mail_id);
				rs = st.executeQuery();
				if(rs.next())
				{
					isValidURL = true;
					passcode_from_db = rs.getString("passcode");
					invitation_status = rs.getString("invitation_status");
					rs = null;
				}
				else
				{
					response.setStatus(402);
				}
				if(isValidURL)
				{
					if(invitation_status.equals("joined"))
					{
						
					}
					else if(passcode.equals(passcode_from_db))
					{
						st1 = con.prepareStatement("insert into adduser(account_id,user_id,role) values(?,?,?);");
						st1.setLong(1, account_id);
						st1.setLong(2, userid);
						st1.setString(3, "user");
						int i;
						i = st1.executeUpdate();
						st1 = con.prepareStatement("update accounts set no_of_members=no_of_members+1 where account_id=?;");
						st1.setLong(1, account_id);
						i = st1.executeUpdate();
						st1 = con.prepareStatement("update invitations set invitation_status='joined' where invitation_id=?;");
						st1.setLong(1, invitation_id);
						i = st1.executeUpdate();
						session.setAttribute("account_id",account_id);
					}
					else if(!passcode.equals(passcode_from_db))
					{
						response.setStatus(401);
					}				
				}
				
				if(rs != null)
				{
					rs.close();
					st.close();
				}
				if(st1!=null)
				{
					st1.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
