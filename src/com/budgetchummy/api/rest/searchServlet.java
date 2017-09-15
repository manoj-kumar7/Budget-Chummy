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
		

		long date = Long.parseLong(request.getParameter("date"));

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
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
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

			st1 = con.prepareStatement("select amount,tag_id from transactions where account_id=? AND date=? AND transaction_type=?;");
			st1.setLong(1, accid);
			st1.setLong(2, date);
			st1.setString(3, "income");
			rs = st1.executeQuery();
			float amount=-1;
			String tag_name=null;
			JSONArray income_arr = new JSONArray();
			JSONObject income_obj = new JSONObject();
			while(rs.next())
			{
				tag_name=tags.get(rs.getLong("tag_id"));
				amount=rs.getFloat("amount");
				income_obj.put("tag_name", tag_name);
				income_obj.put("amount", amount);
				income_arr.add(income_obj.toJSONString());
				income_obj.clear();
			}
			rs=null;

			st1 = con.prepareStatement("select amount,tag_id from transactions where account_id=? AND date=? AND transaction_type=?;");
			st1.setLong(1, accid);
			st1.setLong(2, date);
			st1.setString(3, "expense");
			rs = st1.executeQuery();
			JSONArray expense_arr = new JSONArray();
			JSONObject expense_obj = new JSONObject();	
			while(rs.next())
			{
				tag_name=tags.get(rs.getLong("tag_id"));
				amount=rs.getFloat("amount");
				expense_obj.put("tag_name", tag_name);
				expense_obj.put("amount", amount);
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
