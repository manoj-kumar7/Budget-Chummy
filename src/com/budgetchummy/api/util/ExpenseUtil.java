package com.budgetchummy.api.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.SchedulerException;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.ExpenseUtil;
import com.budgetchummy.api.util.ReminderScheduler;

public class ExpenseUtil {
	public static void getExpenses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			int month = Integer.parseInt(request.getParameter("month"));
			int year = Integer.parseInt(request.getParameter("year"));
			String page = request.getParameter("page");

			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			long accid=-1;
			
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Connection con = null;
			PreparedStatement st=null;	
			ResultSet rs=null;		
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				Object acc_attribute = session.getAttribute("account_id");
				accid = Long.parseLong(String.valueOf(acc_attribute));
				String query="";
				st = con.prepareStatement("select transaction_id,transactions.user_id,users.first_name,date,amount,transactions.tag_id,tags.tag_name,description,location,latitude,longitude,added_date_time,repeat_period,reminder_period from users,tags,transactions where extract(year from to_timestamp(floor(date/1000)))<=? AND transaction_type=? AND transactions.account_id=? AND users.user_id=transactions.user_id AND tags.tag_id=transactions.tag_id;");
				st.setInt(1, year);
				st.setString(2, "expense");
				st.setLong(3, accid);				
				rs = st.executeQuery();
				String description=null,tag_name=null,location=null,first_name=null;
				float amount=-1;
				long transaction_id=-1,user_id=-1,tag_id=-1,added_date_time=-1,date=-1,repeat_period=-1,reminder_period=-1;
				float lat,lon;
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				while(rs.next())
				{
					first_name = rs.getString("first_name");
					tag_id=rs.getLong("tag_id");
					tag_name = rs.getString("tag_name");
					transaction_id=rs.getLong("transaction_id");
					date=rs.getLong("date");
					amount=rs.getFloat("amount");
					description=rs.getString("description");
					location=rs.getString("location");
					lat=rs.getFloat("latitude");
					lon=rs.getFloat("longitude");
					added_date_time=rs.getLong("added_date_time");
					repeat_period=rs.getLong("repeat_period");
					reminder_period=rs.getLong("reminder_period");
					jo.put("transaction_id", transaction_id);	
					jo.put("user_name", first_name);
					jo.put("date", date);
					jo.put("amount", amount);
					jo.put("tag_name", tag_name);
					jo.put("description", description);
					jo.put("location", location);
					jo.put("latitude", lat);
					jo.put("longitude", lon);
					jo.put("added_date_time", added_date_time);
					jo.put("repeat_period", repeat_period);
					jo.put("reminder_period", reminder_period);
					ja.add(jo.toJSONString());
					jo.clear();
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(ja.toString());
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
		}
	}

	public static void addExpense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
	//		String page_name = request.getParameter("page_name");
			float amount = Float.parseFloat(request.getParameter("amount"));
			long date = Long.parseLong(request.getParameter("date"));
			int reminder_day = Integer.parseInt(request.getParameter("reminder_day"));
			int reminder_month = Integer.parseInt(request.getParameter("reminder_month"));
			int reminder_year = Integer.parseInt(request.getParameter("reminder_year"));
			long tag_id = Long.parseLong(request.getParameter("tag_id"));
			long added_date= Long.parseLong(request.getParameter("created_date_time"));
			String transaction_type = "expense";
			String additional_info = request.getParameter("add_info");
			
			
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			Connection con = null;
			PreparedStatement st=null, st1=null;
			ResultSet rs = null;		
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				Object user_attribute = session.getAttribute("user_id");
				Object acc_attribute = session.getAttribute("account_id");
				long userid = Long.parseLong(String.valueOf(user_attribute));
				long accid = Long.parseLong(String.valueOf(acc_attribute));
				String query = null;
				
				boolean reminderExists = false;
				String timezone = "";
				long target_time = -1;
			    // DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
			    // Date dateobj = new Date();
			    // df.setTimeZone(TimeZone.getTimeZone("IST"));
			    // added_date = Datehelper.dateToEpoch(df.format(dateobj));
				if(additional_info.equals("true"))
				{
					String location = request.getParameter("location");
					float latitude = -1, longitude = -1;
					if(request.getParameter("location_lat") != null && request.getParameter("location_lat") != "")
					{
						latitude = Float.parseFloat(request.getParameter("location_lat"));
					}
					if(request.getParameter("location_lon") != null && request.getParameter("location_lon") != "")
					{
						longitude = Float.parseFloat(request.getParameter("location_lon"));
					}
					String description = request.getParameter("description");
					int expense_repeat = Integer.parseInt(request.getParameter("repeat"));
					int expense_reminder = Integer.parseInt(request.getParameter("reminder"));
					st = con.prepareStatement("insert into transactions(user_id,account_id,date,amount,tag_id,description,transaction_type,repeat_period,reminder_period,location,latitude,longitude,added_date_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?);");
					st.setLong(1, userid);
					st.setLong(2, accid);
					st.setLong(3, date);
					st.setFloat(4, amount);
					st.setLong(5, tag_id);
					st.setString(6, description);
					st.setString(7, transaction_type);
					st.setInt(8, expense_repeat);
					st.setInt(9, expense_reminder);
					st.setString(10, location);
					st.setFloat(11, latitude);
					st.setFloat(12, longitude);
					st.setLong(13, added_date);
					if(expense_reminder > 0)
					{
						reminderExists = true;
						st1 = con.prepareStatement("select timezone from accounts where account_id=?;");
						st1.setLong(1, accid);
						rs = st1.executeQuery();
						if(rs.next())
						{
							timezone = rs.getString("timezone");
						}
						rs = null;
						target_time = Datehelper.convertTimeZone(date, timezone);
						if(expense_reminder == 1)
						{
							target_time = Datehelper.subtractDays(target_time, 1);
						}
					}
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
				int i = st.executeUpdate();
				if(reminderExists)
				{
					long job_id = -1;
					st = con.prepareStatement("select lastval();");
					rs = st.executeQuery();
					if(rs.next())
					{
						long transaction_id = rs.getLong(1);
						st = con.prepareStatement("insert into jobs(reminder_type,data_id,do_at) values(?,?,?);");
						st.setString(1, "expense");
						st.setLong(2, transaction_id);
						st.setLong(3, target_time);
						int j = st.executeUpdate();
						rs = null;
						st = con.prepareStatement("select lastval();");
						rs = st.executeQuery();
						if(rs.next())
						{
							job_id = rs.getLong(1);
						}
					}	
					try {
						ReminderScheduler.scheduleReminder(request, reminder_day, reminder_month, reminder_year, timezone, job_id);
					} catch (SchedulerException e) {
						e.printStackTrace();
					}
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
				if(st1 != null)
				{
					try{
						st1.close();
					}catch (SQLException e) { /* ignored */}
				}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
	//		response.sendRedirect("home?page='"+page_name+"'");
		}
	}

	public static void editExpense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
	//		String page_name = request.getParameter("page_name");
			float amount = Float.parseFloat(request.getParameter("amount"));
			long date = Long.parseLong(request.getParameter("date"));
			int reminder_day = Integer.parseInt(request.getParameter("reminder_day"));
			int reminder_month = Integer.parseInt(request.getParameter("reminder_month"));
			int reminder_year = Integer.parseInt(request.getParameter("reminder_year"));
			long tag_id = Long.parseLong(request.getParameter("tag_id"));
			long transaction_id = Long.parseLong(request.getParameter("transaction_id"));
			JobsUtil.deleteJob(transaction_id);
			String transaction_type = "expense";
			String additional_info = request.getParameter("add_info");
			
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			Connection con = null;
			PreparedStatement st=null, st1=null;
			ResultSet rs = null;			
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				Object user_attribute = session.getAttribute("user_id");
				Object acc_attribute = session.getAttribute("account_id");
				long userid = Long.parseLong(String.valueOf(user_attribute));
				long accid = Long.parseLong(String.valueOf(acc_attribute));
				String query=null;

				boolean reminderExists = false;
				String timezone = "";
				long target_time = -1;
				if(additional_info.equals("true"))
				{
					String location = request.getParameter("location");
					float latitude = -1, longitude = -1;
					if(request.getParameter("location_lat") != null && request.getParameter("location_lat") != "")
					{
						latitude = Float.parseFloat(request.getParameter("location_lat"));
					}
					if(request.getParameter("location_lon") != null && request.getParameter("location_lon") != "")
					{
						longitude = Float.parseFloat(request.getParameter("location_lon"));
					}
					String description = request.getParameter("description");
					int expense_repeat = Integer.parseInt(request.getParameter("repeat"));
					int expense_reminder = Integer.parseInt(request.getParameter("reminder"));
					st = con.prepareStatement("update transactions set amount=?,date=?,tag_id=?,location=?,latitude=?,longitude=?,description=?,repeat_period=?,reminder_period=? where transaction_id=?;");
					st.setFloat(1, amount);
					st.setLong(2, date);
					st.setLong(3, tag_id);
					st.setString(4, location);
					st.setFloat(5, latitude);
					st.setFloat(6, longitude);
					st.setString(7, description);
					st.setInt(8, expense_repeat);
					st.setInt(9, expense_reminder);
					st.setLong(10, transaction_id);
					if(expense_reminder > 0)
					{
						reminderExists = true;
						st1 = con.prepareStatement("select timezone from accounts where account_id=?;");
						st1.setLong(1, accid);
						rs = st1.executeQuery();
						if(rs.next())
						{
							timezone = rs.getString("timezone");
						}
						rs = null;
						target_time = Datehelper.convertTimeZone(date, timezone);
						if(expense_reminder == 1)
						{
							target_time = Datehelper.subtractDays(target_time, 1);
						}
					}
				}
				else
				{
					st = con.prepareStatement("update transactions set amount=?,date=?,tag_id=?,location=?,latitude=?,longitude=?,description=?,repeat_period=?,reminder_period=? where transaction_id=?;");
					st.setFloat(1, amount);
					st.setLong(2, date);
					st.setLong(3, tag_id);
					st.setString(4, null);
					st.setFloat(5, 0);
					st.setFloat(6, 0);
					st.setString(7, null);
					st.setInt(8, 0);
					st.setInt(9, 0);
					st.setLong(10, transaction_id);		
				}
				int i = st.executeUpdate();
				if(reminderExists)
				{
					long job_id = -1;
					st = con.prepareStatement("insert into jobs(reminder_type,data_id,do_at) values(?,?,?);");
					st.setString(1, "expense");
					st.setLong(2, transaction_id);
					st.setLong(3, target_time);
					int j = st.executeUpdate();	
					rs = null;
					st = con.prepareStatement("select lastval();");
					rs = st.executeQuery();
					if(rs.next())
					{
						job_id = rs.getLong(1);
					}	
					try {
						ReminderScheduler.scheduleReminder(request, reminder_day, reminder_month, reminder_year, timezone, job_id);
					} catch (SchedulerException e) {
						e.printStackTrace();
					}			
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
				if(st1 != null)
				{
					try{
						st1.close();
					}catch (SQLException e) { /* ignored */}
				}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
		}
	}

	public static void deleteExpense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			long transaction_id = Long.parseLong(request.getParameter("transaction_id"));
			JobsUtil.deleteJob(transaction_id);
			String transaction_type = "expense";
			
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
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				Object user_attribute = session.getAttribute("user_id");
				Object acc_attribute = session.getAttribute("account_id");
				long userid = Long.parseLong(String.valueOf(user_attribute));
				long accid = Long.parseLong(String.valueOf(acc_attribute));
				String query=null;
				st = con.prepareStatement("delete from transactions where transaction_id=?");
				st.setLong(1, transaction_id);		
				int i = st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try{
					st.close();
				}catch (SQLException e) { /* ignored */}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
		}
	}
}
