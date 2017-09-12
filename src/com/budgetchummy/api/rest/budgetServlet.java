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

@WebServlet(urlPatterns = {"/budget", "/BudgetChummy/budget"})
public class budgetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public budgetServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String month = request.getParameter("month");
		String year = request.getParameter("year");
		
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
			Statement st=null, st1=null, st2=null;
			st = con.createStatement();
			st1 = con.createStatement();
			st2 = con.createStatement();
			ResultSet rs = null, rs1 = null, rs2 = null;
			
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			Object acc_attribute = session.getAttribute("account_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			long accid = Long.parseLong(String.valueOf(acc_attribute));
			Map<Long, String> tags = new HashMap<Long, String>();
			
			String query = "select tag_id, tag_name from tags where account_id="+accid+";";
			rs = st.executeQuery(query);
			while(rs.next())
			{
				tags.put(rs.getLong("tag_id"), rs.getString("tag_name"));
			}
			query = "select amount, repeat_period, tag_id, description, added_by, budget_type, start_date, end_date from budget where (extract(year from to_timestamp(floor(start_date/1000)))<"+year+" OR (extract(year from to_timestamp(floor(start_date/1000)))="+year+" AND extract(month from to_timestamp(floor(start_date/1000)))<="+month+")) AND account_id="+accid+";";
			rs1 = st1.executeQuery(query);
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
				query="select first_name from users where user_id="+added_by+";";
				rs2=st2.executeQuery(query);
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
				rs.close();
			if(rs1 != null)
				rs1.close();
			if(rs2 != null)
				rs2.close();
			st.close();
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
			Statement st=null;
			st = con.createStatement();
			HttpSession session = request.getSession();
			Object user_attribute = session.getAttribute("user_id");
			Object acc_attribute = session.getAttribute("account_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			long accid = Long.parseLong(String.valueOf(acc_attribute));
			String query = "insert into budget(account_id,budget_type,tag_id,repeat_period,amount,description,added_by,start_date,end_date) values("+accid+","+budget_type+","+tag_id+","+budget_repeat+","+budget_amount+",'"+budget_description+"',"+userid+","+start_date+","+end_date+");";
			
			st.executeUpdate(query);
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		response.sendRedirect("home.jsp?page='"+page_name+"'");
		
	}

}
