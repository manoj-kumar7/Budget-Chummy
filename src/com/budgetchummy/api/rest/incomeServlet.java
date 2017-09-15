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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;


@WebServlet(urlPatterns = {"/api/v1/income", "/BudgetChummy/api/v1/income"})
public class incomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public incomeServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int month = Integer.parseInt(request.getParameter("month"));
		int year = Integer.parseInt(request.getParameter("year"));

		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		long accid=-1;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null,st1=null,st2=null;
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
			Object acc_attribute = session.getAttribute("account_id");
			accid = Long.parseLong(String.valueOf(acc_attribute));
			st = con.prepareStatement("select user_id,date,amount,tag_id,description,location,latitude,longitude,added_date_time from transactions where extract(year from to_timestamp(floor(date/1000)))=? AND extract(month from to_timestamp(floor(date/1000)))=? AND transaction_type=? AND account_id=?;");
			st.setInt(1, year);
			st.setInt(2, month);
			st.setString(3, "income");
			st.setLong(4, accid);	
			ResultSet rs=null,rs1=null,rs2=null;
			rs = st.executeQuery();
			String description=null,tag_name=null,location=null,first_name=null;
			float amount=-1;
			long user_id=-1,tag_id=-1,added_date_time=-1,date=-1;
			float lat,lon;
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			while(rs.next())
			{

				user_id=rs.getLong("user_id");
				st1 = con.prepareStatement("select first_name from users where user_id=?;");
				st1.setLong(1, user_id);
				rs1=st1.executeQuery();
				while(rs1.next())
				{
					first_name = rs1.getString("first_name");
				}
				tag_id=rs.getLong("tag_id");
				st2 = con.prepareStatement("select tag_name from tags where tag_id=?;");
				st2.setLong(1, tag_id);
				rs2=st2.executeQuery();
				while(rs2.next())
				{
					tag_name = rs2.getString("tag_name");
				}
				date=rs.getLong("date");
				amount=rs.getFloat("amount");
				description=rs.getString("description");
				location=rs.getString("location");
				lat=rs.getFloat("latitude");
				lon=rs.getFloat("longitude");
				added_date_time=rs.getLong("added_date_time");				
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
			if(rs != null)
			{
				rs.close();
				st.close();
			}
			if(rs1!=null)
			{
				rs1.close();
				st1.close();
			}
			if(rs2!=null)
			{
				rs2.close();
				st2.close();
			}
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
//		String page_name = request.getParameter("page_name");
		float amount = Float.parseFloat(request.getParameter("amount"));
		long date = Long.parseLong(request.getParameter("date"));
		long tag_id = Long.parseLong(request.getParameter("tag_id"));
		long added_date=0;
		String transaction_type = "income";
		String additional_info = request.getParameter("add_info");
		
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
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
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
				String location = request.getParameter("location");
				float latitude = Float.parseFloat(request.getParameter("location_lat"));
				float longitude = Float.parseFloat(request.getParameter("location_lon"));
				String description = request.getParameter("description");
				int income_repeat = Integer.parseInt(request.getParameter("repeat"));
				int income_reminder = Integer.parseInt(request.getParameter("reminder"));
				st = con.prepareStatement("insert into transactions(user_id,account_id,date,amount,tag_id,description,transaction_type,repeat_period,reminder_period,location,latitude,longitude,added_date_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?);");
				st.setLong(1, userid);
				st.setLong(2, accid);
				st.setLong(3, date);
				st.setFloat(4, amount);
				st.setLong(5, tag_id);
				st.setString(6, description);
				st.setString(7, transaction_type);
				st.setInt(8, income_repeat);
				st.setInt(9, income_reminder);
				st.setString(10, location);
				st.setFloat(11, latitude);
				st.setFloat(12, longitude);
				st.setLong(13, added_date);
			}
			else
			{
				st = con.prepareStatement("insert into transactions(user_id,account_id,date,amount,tag_id,transaction_type,added_date_time) values(?,?,?,?,?,?,?);");
				st.setLong(1, userid);
				st.setLong(2, accid);
				st.setLong(3, date);
				st.setFloat(4, amount);
				st.setLong(5, tag_id);
				st.setString(6, transaction_type);
				st.setLong(7, added_date);			
			}
			st.executeUpdate();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		response.sendRedirect("home?page='"+page_name+"'");
	}

}
