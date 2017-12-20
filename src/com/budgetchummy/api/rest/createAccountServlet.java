package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.budgetchummy.api.util.APIConstants;


@WebServlet(urlPatterns = {"/api/v1/createAccount", "/BudgetChummy/api/v1/createAccount"})
public class createAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public createAccountServlet() {
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
			response.setStatus(401);
		}
		else
		{
			String account_name = request.getParameter("account_name");
			String timezone = request.getParameter("timezone");
			long added_date= Long.parseLong(request.getParameter("created_date_time"));
			String role = "admin";
			long userid=0,accountid=0;
			
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			Connection con = null;
			PreparedStatement st=null;
			ResultSet rs = null;
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				String query=null;
			    // DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			    // Date dateobj = new Date();
			    // df.setTimeZone(TimeZone.getTimeZone("IST"));
				// added_date = Datehelper.dateToEpoch(df.format(dateobj));
				Object user_attribute = session.getAttribute("user_id");
				userid = Long.parseLong(String.valueOf(user_attribute));

				st = con.prepareStatement("insert into accounts(account_name,created_by,no_of_members,created_date_time,timezone) values(?,?,?,?,?);");
				st.setString(1, account_name);
				st.setLong(2, userid);
				st.setInt(3, 1);
				st.setLong(4, added_date);
				st.setString(5, timezone);
				int i = st.executeUpdate();
				st = con.prepareStatement("select lastval();");
				rs = st.executeQuery();
				if(rs.next())
				{
					accountid = rs.getInt(1);
					st = con.prepareStatement("insert into adduser(account_id,user_id,role) values(?,?,?);");
					st.setLong(1, accountid);
					st.setLong(2, userid);
					st.setString(3, "admin");
					int j = st.executeUpdate();
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
			session.setAttribute("account_id",accountid);
	//		String homeurl = new String("home");
	//		response.setStatus(response.SC_MOVED_TEMPORARILY);
	//        response.setHeader("Location", homeurl);
		}
		
	}

}
