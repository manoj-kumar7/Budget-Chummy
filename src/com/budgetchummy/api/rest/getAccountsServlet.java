package com.budgetchummy.api.rest;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.APIConstants;


@WebServlet(urlPatterns = {"/api/v1/getAccounts", "/BudgetChummy/api/v1/getAccounts"})
public class getAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public getAccountsServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(401);
		}
		else
		{
			String page = request.getParameter("page");
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			boolean verified = false;
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
				Object attribute = session.getAttribute("user_id");
				long userid = Long.parseLong(String.valueOf(attribute));
				long accid=-1;
				if(page.equals("home"))
				{
					Object acc_attribute = session.getAttribute("account_id");
					accid = Long.parseLong(String.valueOf(acc_attribute));				
				}
				String query=null;
				st = con.prepareStatement("select verified from users where user_id=?");
				st.setLong(1, userid);
				rs = st.executeQuery();
				if(rs.next())
				{
					verified = rs.getBoolean("verified");
					rs = null;
				}
				if(verified)
				{
					st = con.prepareStatement("select accounts.account_name,adduser.account_id from adduser,accounts where user_id=? AND accounts.account_id=adduser.account_id;");
					st.setLong(1, userid);
					rs = st.executeQuery();
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					long acc_id = 0;
					String acc_name;
					while(rs.next())
					{
						acc_id = rs.getInt("account_id");
						acc_name = rs.getString("account_name");
						jo.put("account_id", acc_id);
						jo.put("account_name",acc_name);
						ja.add(jo.toJSONString());
					}
					jo.clear();
					if(page.equals("home"))
					{
						jo.put("current_account", accid);
						ja.add(jo.toJSONString());
					}
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print(ja.toString());
				}
				else
				{
					response.setStatus(403);
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
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
