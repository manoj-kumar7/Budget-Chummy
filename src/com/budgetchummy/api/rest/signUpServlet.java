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
import com.budgetchummy.api.util.Datehelper;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
/**
 * Servlet implementation class signUpServlet
 */
@WebServlet("/signUpServlet")
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
		String phone_no = request.getParameter("phone_no");
		String pword = request.getParameter("pword");
		String account_id = request.getParameter("account_id");
		String invitation_id = request.getParameter("invitation_id");
		long userid=0,added_date=0;
		
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
			Statement st=null;
			st = con.createStatement();
			ResultSet rs = null;
			String query=null;

		    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    Date dateobj = new Date();
		    df.setTimeZone(TimeZone.getTimeZone("IST"));
		    added_date = Datehelper.dateToEpoch(df.format(dateobj));
			query = "insert into users(first_name,last_name,email,phone_no,password,created_date_time) values('"+first_name+"','"+last_name+"','"+email+"','"+phone_no+"','"+pword+"',"+added_date+");";
			st.executeUpdate(query);
			query = "select user_id from users where email='"+email+"';";
			rs = st.executeQuery(query);

			while(rs.next())
			{
				userid = rs.getInt("user_id");
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		HttpSession session = request.getSession();
		session.setAttribute("useremail",email);
		session.setAttribute("user_id",userid);
		
		if(account_id.equals("null") || invitation_id.equals("null") || account_id.equals(null) || invitation_id.equals(null))
		{
			String homeurl = new String("CreateAccount.jsp");
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

}
