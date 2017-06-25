package com.budgetchummy.api.rest;


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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@WebServlet("/getAccountsServlet")
public class getAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public getAccountsServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String url = "https://mysql32017-budgetchummy.cloud.cms500.com";
		String user = "root";
		String mysql_password = "YXStrl85124";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null,st1=null;
			st = con.createStatement();
			st1 = con.createStatement();
			HttpSession session = request.getSession();
			Object attribute = session.getAttribute("user_id");
			long userid = Long.parseLong(String.valueOf(attribute));
			String query=null;
			query = "select account_id from adduser where user_id="+userid+";";
			ResultSet rs = st.executeQuery(query);
			ResultSet rs1 = null;
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			long acc_id = 0;
			while(rs.next())
			{
				acc_id = rs.getInt("account_id");
				jo.put("account_id", acc_id);
				query = "select account_name from accounts where account_id="+acc_id+";";
				rs1 = st1.executeQuery(query);
				while(rs1.next())
				{
					String acc_name = rs1.getString("account_name");
					jo.put("account_name",acc_name);
					ja.add(jo.toJSONString());
				}
			}
			rs.close();
			rs1.close();
			st.close();
			st1.close();
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
