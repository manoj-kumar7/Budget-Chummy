package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;


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
		String account_name = request.getParameter("account_name");
		String role = "admin";
		long userid=0,accountid=0;
		long added_date=0;
		
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
			PreparedStatement st=null;
			ResultSet rs = null;
			String query=null;

		    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		    Date dateobj = new Date();
		    df.setTimeZone(TimeZone.getTimeZone("IST"));
		    
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
			Object user_attribute = session.getAttribute("user_id");
			userid = Long.parseLong(String.valueOf(user_attribute));
			added_date = Datehelper.dateToEpoch(df.format(dateobj));

			st = con.prepareStatement("insert into accounts(account_name,created_by,no_of_members,created_date_time) values(?,?,?,?);");
			st.setString(1, account_name);
			st.setLong(2, userid);
			st.setInt(3, 1);
			st.setLong(4, added_date);
			int i = st.executeUpdate();
			st = con.prepareStatement("select lastval();");
			rs = st.executeQuery();
			if(rs.next())
			{
				accountid = rs.getInt(1);
				st = con.prepareStatement("insert into adduser(account_id,user_id,role) values("+accountid+","+userid+",'admin');");
				st.setLong(1, accountid);
				st.setLong(2, userid);
				st.setString(3, "admin");
				int j = st.executeUpdate();
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		HttpSession session = request.getSession();
		session.setAttribute("account_id",accountid);
//		String homeurl = new String("home");
//		response.setStatus(response.SC_MOVED_TEMPORARILY);
//        response.setHeader("Location", homeurl);
	}

}
