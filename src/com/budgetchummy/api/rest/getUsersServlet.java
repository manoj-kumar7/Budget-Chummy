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


@WebServlet(urlPatterns = {"/api/v1/getUsers", "/BudgetChummy/api/v1/getUsers"})
public class getUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public getUsersServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		long accid=-1;
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			PreparedStatement st=null,st1=null;
			ResultSet rs = null,rs1=null;
			String query=null;
			String account_name=null,created_by=null,created_date_time=null;
			String role=null,first_name=null,email=null;
			long no_of_members=-1,created_by_id = -1,user_id=-1;
			HttpSession session = request.getSession(false);
			if(session == null)
			{
				response.sendRedirect("login");
			}
			Object acc_attribute = session.getAttribute("account_id");
			accid = Long.parseLong(String.valueOf(acc_attribute));
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			st = con.prepareStatement("select account_name,created_by,no_of_members,created_date_time from accounts where account_id=?;");
			st.setLong(1, accid);
			rs = st.executeQuery();
			while(rs.next())
			{
				account_name = rs.getString("account_name");
				created_by_id = rs.getLong("created_by");
				no_of_members = rs.getLong("no_of_members");
				created_date_time = Datehelper.epochToDate(rs.getLong("created_date_time"));
			}

			st = con.prepareStatement("select first_name from users where user_id=?;");
			st.setLong(1, created_by_id);
			rs = st.executeQuery();
			while(rs.next())
			{
				created_by = rs.getString("first_name");
			}
			jo.put("account_name",account_name);
			jo.put("no_of_members",no_of_members);
			jo.put("created_by",created_by);
			jo.put("created_date_time",created_date_time);
			ja.add(jo.toJSONString());
			jo.clear();
			
			st = con.prepareStatement("select user_id,role from adduser where account_id=?;");
			st.setLong(1, accid);
			rs = st.executeQuery();
			while(rs.next())
			{
				user_id = rs.getInt("user_id");
				role = rs.getString("role");
				st1 = con.prepareStatement("select first_name,email from users where user_id=?;");
				st1.setLong(1, user_id);
				rs1 = st1.executeQuery();
				while(rs1.next())
				{
					first_name = rs1.getString("first_name");
					email = rs1.getString("email");
				}
				jo.put("first_name",first_name);
				jo.put("email",email);
				jo.put("role",role);
				ja.add(jo.toJSONString());
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
			con.close();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(ja.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
