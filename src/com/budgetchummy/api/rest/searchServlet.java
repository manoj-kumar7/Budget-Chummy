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
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.*;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;

@WebServlet(urlPatterns = {"/api/v1/search", "/BudgetChummy/api/v1/search"})
public class searchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public searchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			long date_from = Long.parseLong(request.getParameter("date_from"));
			long date_to = Long.parseLong(request.getParameter("date_to"));
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			long userid=-1;
			long accid=-1;
			
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				Connection con = null;
				con = DriverManager.getConnection(url,user,mysql_password);
				PreparedStatement st=null, st1=null;
				ResultSet rs=null;
				Object user_attribute = session.getAttribute("user_id");
				Object acc_attribute = session.getAttribute("account_id");
				userid = Long.parseLong(String.valueOf(user_attribute));
				accid = Long.parseLong(String.valueOf(acc_attribute));
				Map<Long, String> tags = new HashMap<Long, String>();

				st = con.prepareStatement("select tag_id, tag_name from tags where account_id=?;");
				st.setLong(1, accid);
				rs = st.executeQuery();
				while(rs.next())
				{
					tags.put(rs.getLong("tag_id"), rs.getString("tag_name"));
				}
				rs=null;

				st1 = con.prepareStatement("select user_id,date,amount,tag_id,description,location,latitude,longitude,added_date_time,repeat_period,reminder_period from transactions where account_id=? AND date<=? AND transaction_type=?;");
				st1.setLong(1, accid);
				st1.setLong(2, date_to);
				st1.setString(3, "income");
				rs = st1.executeQuery();
				String description=null,tag_name=null,location=null,first_name=null;
				float amount=-1;
				long user_id=-1,tag_id=-1,added_date_time=-1,date=-1,repeat_period=-1,reminder_period=-1;
				float lat,lon;
				JSONArray income_arr = new JSONArray();
				JSONObject income_obj = new JSONObject();
				while(rs.next())
				{
					tag_name=tags.get(rs.getLong("tag_id"));
					amount=rs.getFloat("amount");
					user_id=rs.getLong("user_id");
					date=rs.getLong("date");
					description=rs.getString("description");
					location=rs.getString("location");
					lat=rs.getFloat("latitude");
					lon=rs.getFloat("longitude");
					added_date_time=rs.getLong("added_date_time");	
					repeat_period=rs.getLong("repeat_period");
					reminder_period=rs.getLong("reminder_period");

					income_obj.put("tag_name", tag_name);
					income_obj.put("amount", amount);
					income_obj.put("user_id", user_id);
					income_obj.put("date", date);
					income_obj.put("description", description);
					income_obj.put("location", location);
					income_obj.put("latitude", lat);
					income_obj.put("longitude", lon);
					income_obj.put("added_date_time", added_date_time);
					income_obj.put("repeat_period", repeat_period);
					income_obj.put("reminder_period", reminder_period);

					income_arr.add(income_obj.toJSONString());
					income_obj.clear();
				}
				rs=null;

				st1 = con.prepareStatement("select user_id,date,amount,tag_id,description,location,latitude,longitude,added_date_time,repeat_period,reminder_period from transactions where account_id=? AND date<=? AND transaction_type=?;");
				st1.setLong(1, accid);
				st1.setLong(2, date_to);
				st1.setString(3, "expense");
				rs = st1.executeQuery();
				JSONArray expense_arr = new JSONArray();
				JSONObject expense_obj = new JSONObject();	
				while(rs.next())
				{
					tag_name=tags.get(rs.getLong("tag_id"));
					amount=rs.getFloat("amount");
					user_id=rs.getLong("user_id");
					date=rs.getLong("date");
					description=rs.getString("description");
					location=rs.getString("location");
					lat=rs.getFloat("latitude");
					lon=rs.getFloat("longitude");
					added_date_time=rs.getLong("added_date_time");	
					repeat_period=rs.getLong("repeat_period");
					reminder_period=rs.getLong("reminder_period");

					expense_obj.put("tag_name", tag_name);
					expense_obj.put("amount", amount);
					expense_obj.put("user_id", user_id);
					expense_obj.put("date", date);
					expense_obj.put("description", description);
					expense_obj.put("location", location);
					expense_obj.put("latitude", lat);
					expense_obj.put("longitude", lon);
					expense_obj.put("added_date_time", added_date_time);
					expense_obj.put("repeat_period", repeat_period);
					expense_obj.put("reminder_period", reminder_period);

					expense_arr.add(expense_obj.toJSONString());
					expense_obj.clear();
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
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				JSONObject final_obj = new JSONObject();
				final_obj.put("income_data", income_arr);
				final_obj.put("expense_data", expense_arr);
				response.getWriter().print(final_obj.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
