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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.Datehelper;


@WebServlet("/incomeServlet")
public class incomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public incomeServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String month = request.getParameter("month");
		String year = request.getParameter("year");

		String url = "jdbc:mysql://localhost:3306/budgetchummy";
		String user = "root";
		String mysql_password = "manoj";
		long accid=-1;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null,st1=null,st2=null;
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			HttpSession session = request.getSession();
			Object acc_attribute = session.getAttribute("account_id");
			accid = Long.parseLong(String.valueOf(acc_attribute));
			String query = "select user_id,date,amount,tag_id,description,location,latitude,longitude,added_date_time from transactions where account_id="+accid+" AND MONTH(from_unixtime(floor(date/1000)))="+month+" AND YEAR(from_unixtime(floor(date/1000))="+year+" AND transaction_type='income';";
			ResultSet rs=null,rs1=null,rs2=null;
			rs = st.executeQuery(query);
			String date=null,description=null,tag_name=null,location=null,added_date_time=null,first_name=null;
			float amount=-1;
			long user_id=-1,tag_id=-1;
			float lat,lon;
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			while(rs.next())
			{

				user_id=rs.getLong("user_id");
				query="select first_name from users where user_id="+user_id+";";
				rs1=st1.executeQuery(query);
				while(rs1.next())
				{
					first_name = rs1.getString("first_name");
				}
				tag_id=rs.getLong("tag_id");
				query="select tag_name from tags where tag_id="+tag_id+";";
				rs2=st2.executeQuery(query);
				while(rs2.next())
				{
					tag_name = rs2.getString("tag_name");
				}
				date=Datehelper.epochToDate(rs.getLong("date"));
				amount=rs.getFloat("amount");
				description=rs.getString("description");
				location=rs.getString("location");
				lat=rs.getFloat("latitude");
				lon=rs.getFloat("longitude");
				added_date_time=Datehelper.epochToDate(rs.getLong("added_date_time"));				
				jo.put("user_name", first_name);
				jo.put("date", date);
				jo.put("amount", amount);
				jo.put("tag_name", tag_name);
				jo.put("description", description);
				jo.put("location", location);
				jo.put("latitude", lat);
				jo.put("longitude", lon);
				jo.put("added_date_time", added_date_time);
				ja.add(jo.toJSONString());
				jo.clear();
			}
			rs.close();
			if(rs1!=null)
				rs1.close();
			if(rs2!=null)
				rs2.close();
			st.close();
			st1.close();
			st2.close();
			con.close();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(ja.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String page_name = request.getParameter("page_name");
		float amount = Float.parseFloat(request.getParameter("income-amount"));
		long date = Datehelper.dateToEpoch(request.getParameter("income-date"));
		long tag_id = Long.parseLong(request.getParameter("tag_id"));
		long added_date=0;
		String transaction_type = "income";
		String additional_info = request.getParameter("income-additional-info");
		
		String url = "jdbc:mysql://localhost:3306/budgetchummy";
		String user = "root";
		String mysql_password = "manoj";
		
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
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			Object acc_attribute = session.getAttribute("account_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			long accid = Long.parseLong(String.valueOf(acc_attribute));
			String query=null;
		    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		    Date dateobj = new Date();
		    df.setTimeZone(TimeZone.getTimeZone("IST"));
		    added_date = Datehelper.dateToEpoch(df.format(dateobj));
			if(additional_info.equals("true"))
			{
				String location = request.getParameter("income-location");
				float latitude = Float.parseFloat(request.getParameter("income-location-lat"));
				float longitude = Float.parseFloat(request.getParameter("income-location-lon"));
				String description = request.getParameter("income-description");
				int income_repeat = Integer.parseInt(request.getParameter("income-repeat"));
				int income_reminder = Integer.parseInt(request.getParameter("income-reminder"));
				query = "insert into transactions(user_id,account_id,date,amount,tag_id,description,transaction_type,repeat_period,reminder_period,location,latitude,longitude,added_date_time) values("+userid+","+accid+","+date+","+amount+","+tag_id+",'"+description+"','"+transaction_type+"',"+income_repeat+","+income_reminder+",'"+location+"',"+latitude+","+longitude+","+added_date+");";
			}
			else
			{
				query = "insert into transactions(user_id,account_id,date,amount,tag_id,transaction_type,added_date_time) values("+userid+","+accid+","+date+","+amount+","+tag_id+",'"+transaction_type+"',"+added_date+");";
			}
			st.executeUpdate(query);
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("home.jsp?page='"+page_name+"'");
	}

}
