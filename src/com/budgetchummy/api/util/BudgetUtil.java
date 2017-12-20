package com.budgetchummy.api.util;

import com.budgetchummy.api.util.APIConstants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BudgetUtil {
	public static void getBudgets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			int month = Integer.parseInt(request.getParameter("month"));
			int year = Integer.parseInt(request.getParameter("year"));
			
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
				Object user_attribute = session.getAttribute("user_id");
				Object acc_attribute = session.getAttribute("account_id");
				long userid = Long.parseLong(String.valueOf(user_attribute));
				long accid = Long.parseLong(String.valueOf(acc_attribute));
				Map<Long, String> tags = new HashMap<Long, String>();
				
				st = con.prepareStatement("select tag_id, tag_name from tags where account_id=?;");
				st.setLong(1, accid);
				rs = st.executeQuery();
				while(rs.next())
				{
					tags.put(rs.getLong("tag_id"), rs.getString("tag_name"));
				}

				rs = null;
				st = con.prepareStatement("select budget_id, amount, repeat_period, tag_id, description, added_by, users.first_name, budget_type, start_date, end_date from users, budget where (extract(year from to_timestamp(floor(start_date/1000)))<? OR (extract(year from to_timestamp(floor(start_date/1000)))=? AND extract(month from to_timestamp(floor(start_date/1000)))<=?)) AND account_id=? AND users.user_id=budget.added_by;");
				st.setInt(1, year);
				st.setInt(2, year);
				st.setInt(3, month);
				st.setLong(4, accid);
				rs = st.executeQuery();
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				String description, tag_name, added_by_name="";
				long added_by, start_date, end_date, budget_id=-1;
				float amount;
				int repeat_period, budget_type;
				while(rs.next())
				{
					budget_id=rs.getLong("budget_id");
					start_date=rs.getLong("start_date");
					end_date=rs.getLong("end_date");
					amount=rs.getFloat("amount");
					description=rs.getString("description");
					tag_name=tags.get(rs.getLong("tag_id"));
					added_by=rs.getLong("added_by");
					added_by_name = rs.getString("first_name");
					repeat_period=rs.getInt("repeat_period");
					budget_type=rs.getInt("budget_type");
					jo.put("budget_id", budget_id);
					jo.put("start_date", start_date);
					jo.put("end_date", end_date);
					jo.put("amount", amount);
					jo.put("tag_name", tag_name);
					jo.put("description", description);
					jo.put("added_by_name", added_by_name);
					jo.put("repeat_period", repeat_period);
					jo.put("budget_type", budget_type);
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

	public static void addBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
			int budget_type = Integer.parseInt(request.getParameter("budget_type"));
			long tag_id=-1;
			if(budget_type == 1)
			{
				tag_id = Long.parseLong(request.getParameter("tag_id"));
			}
			int budget_repeat = Integer.parseInt(request.getParameter("budget_repeat"));
			long start_date=-1,end_date=-1;

		    start_date = Long.parseLong(request.getParameter("budget_start_date"));

			if(budget_repeat == 0)
			{
				end_date = Long.parseLong(request.getParameter("budget_end_date"));
			}

			float budget_amount = Float.parseFloat(request.getParameter("budget_amount"));
			String budget_description = request.getParameter("budget_description");
			
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
				st = con.prepareStatement("insert into budget(account_id,budget_type,tag_id,repeat_period,amount,description,added_by,start_date,end_date) values(?,?,?,?,?,?,?,?,?);");
				st.setLong(1, accid);
				st.setInt(2, budget_type);
				st.setLong(3, tag_id);
				st.setInt(4, budget_repeat);
				st.setFloat(5, budget_amount);
				st.setString(6, budget_description);
				st.setLong(7, userid);
				st.setLong(8, start_date);
				st.setLong(9, end_date);
				int i = st.executeUpdate();
				st.close();
				con.close();
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
	//		response.sendRedirect("home?page='"+page_name+"'");
		}
	}

	public static void editBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			long budget_id = Long.parseLong(request.getParameter("budget_id"));
			int budget_type = Integer.parseInt(request.getParameter("budget_type"));
			long tag_id=-1;
			if(budget_type == 1)
			{
				tag_id = Long.parseLong(request.getParameter("tag_id"));
			}
			int budget_repeat = Integer.parseInt(request.getParameter("budget_repeat"));
			long start_date=-1,end_date=-1;

		    start_date = Long.parseLong(request.getParameter("budget_start_date"));

			if(budget_repeat == 0)
			{
				end_date = Long.parseLong(request.getParameter("budget_end_date"));
			}

			float budget_amount = Float.parseFloat(request.getParameter("budget_amount"));
			String budget_description = request.getParameter("budget_description");
			
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
				st = con.prepareStatement("update budget set budget_type=?,tag_id=?,repeat_period=?,amount=?,description=?,start_date=?,end_date=? where budget_id=?;");
				st.setInt(1, budget_type);
				st.setLong(2, tag_id);
				st.setInt(3, budget_repeat);
				st.setFloat(4, budget_amount);
				st.setString(5, budget_description);
				st.setLong(6, start_date);
				st.setLong(7, end_date);
				st.setLong(8, budget_id);
				int i = st.executeUpdate();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try{
					st.close();
				}catch (SQLException e) { /* ignored */}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
	//		response.sendRedirect("home?page='"+page_name+"'");
		}
	}

	public static void deleteBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			long budget_id = Long.parseLong(request.getParameter("budget_id"));
			
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
				st = con.prepareStatement("delete from budget where budget_id=?");
				st.setLong(1, budget_id);		
				int i = st.executeUpdate();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
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
