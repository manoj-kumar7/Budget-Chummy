package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.APIConstants;

/**
 * Servlet implementation class tagsServlet
 */
@WebServlet("/tagsServlet")
public class tagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public tagsServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String url = APIConstants.MYSQL_URL;
		String user = APIConstants.MYSQL_USERNAME;
		String mysql_password = APIConstants.MYSQL_PASSWORD;
		
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
			Object account_attribute = session.getAttribute("account_id");
			long accid = Long.parseLong(String.valueOf(account_attribute));
			String query=null,tag_name=null;
			long tag_id=-1;
			query = "select tag_id,tag_name from tags where account_id="+accid+";";
			ResultSet rs = st.executeQuery(query);
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			while(rs.next())
			{
				tag_id = rs.getLong("tag_id");
				tag_name = rs.getString("tag_name");
				jo.put("tag_id", tag_id);
				jo.put("tag_name",tag_name);
				ja.add(jo.toJSONString());
				jo.clear();
			}
			rs.close();
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
		String url = APIConstants.MYSQL_URL;
		String user = APIConstants.MYSQL_USERNAME;
		String mysql_password = APIConstants.MYSQL_PASSWORD;
		String tag_name = request.getParameter("tag_name");
		
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
			Object account_attribute = session.getAttribute("account_id");
			long accid = Long.parseLong(String.valueOf(account_attribute));
			String query=null;
			query = "insert into tags(account_id,tag_name) values("+accid+",'"+tag_name+"')";
			st.executeUpdate(query);
			st.close();
			con.close();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
