package com.budgetchummy.api.rest;
import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(urlPatterns = {"/api/v1/budget", "/BudgetChummy/api/v1/budget"})
public class budgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public budgetServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
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
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null, st1=null, st2=null;
			ResultSet rs = null, rs1 = null, rs2 = null;
			
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
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

			st1 = con.prepareStatement("select amount, repeat_period, tag_id, description, added_by, budget_type, start_date, end_date from budget where (extract(year from to_timestamp(floor(start_date/1000)))<? OR (extract(year from to_timestamp(floor(start_date/1000)))=? AND extract(month from to_timestamp(floor(start_date/1000)))<=?)) AND account_id=?;");
			st1.setInt(1, year);
			st1.setInt(2, year);
			st1.setInt(3, month);
			st1.setLong(4, accid);
			rs1 = st1.executeQuery();
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			String description, tag_name, added_by_name="";
			long added_by, start_date, end_date;
			float amount;
			int repeat_period, budget_type;
			while(rs1.next())
			{
				start_date=rs1.getLong("start_date");
				end_date=rs1.getLong("end_date");
				amount=rs1.getFloat("amount");
				description=rs1.getString("description");
				tag_name=tags.get(rs1.getLong("tag_id"));
				added_by=rs1.getLong("added_by");
				repeat_period=rs1.getInt("repeat_period");
				budget_type=rs1.getInt("budget_type");
				st2 = con.prepareStatement("select first_name from users where user_id=?;");
				st2.setLong(1, added_by);
				rs2=st2.executeQuery();
				while(rs2.next())
				{
					added_by_name = rs2.getString("first_name");
				}
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
		int budget_type = Integer.parseInt(request.getParameter("budget_type"));
		long tag_id=-1;
		if(budget_type == 1)
		{
			tag_id = Long.parseLong(request.getParameter("tag_id"));
		}
		int budget_repeat = Integer.parseInt(request.getParameter("budget_repeat"));
		long start_date=-1,end_date=-1;
	    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	    Date dateobj = new Date();
	    df.setTimeZone(TimeZone.getTimeZone("IST"));
	    start_date = Datehelper.dateToEpoch(df.format(dateobj));

		if(budget_repeat == 0)
		{
			start_date = Long.parseLong(request.getParameter("budget_start_date"));
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
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null;
			HttpSession session = request.getSession(false);
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
		}
//		response.sendRedirect("home?page='"+page_name+"'");
		
	}

}
