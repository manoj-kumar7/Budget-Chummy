package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.budgetchummy.api.util.Datehelper;


@WebServlet("/createAccountServlet")
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
		
		String url = "https://mysql32017-budgetchummy.cloud.cms500.com";
		String user = "root";
		String mysql_password = "YXStrl85124";
		

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null;
			st = con.createStatement();
			ResultSet rs = null;
			String query=null;

		    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		    Date dateobj = new Date();
		    df.setTimeZone(TimeZone.getTimeZone("IST"));
		    
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			userid = Long.parseLong(String.valueOf(user_attribute));
			added_date = Datehelper.dateToEpoch(df.format(dateobj));

			query = "insert into accounts(account_name,created_by,no_of_members,created_date_time) values('"+account_name+"',"+userid+","+1+","+added_date+");";
			st.executeUpdate(query);
			query = "select LAST_INSERT_ID();";
			rs = st.executeQuery(query);
			if(rs.next())
			{
				accountid = rs.getInt(1);
				query = "insert into adduser(account_id,user_id,role) values("+accountid+","+userid+",'admin');";
			}
			rs.close();
			st.executeUpdate(query);
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		HttpSession session = request.getSession();
		session.setAttribute("account_id",accountid);
		String homeurl = new String("/home.jsp");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", homeurl);
	}

}
