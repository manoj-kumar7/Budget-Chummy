package com.budgetchummy.api.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

/**
 * Servlet implementation class invitationsServlet
 */
@WebServlet(urlPatterns = {"/api/v1/invitations", "/BudgetChummy/api/v1/invitations"})
public class invitationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public invitationsServlet() {
        super();
        // TODO Auto-generated constructor stub
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
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			long accid=-1;
			
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
				String query=null;
				String sent_to=null,passcode=null;
				Object acc_attribute = session.getAttribute("account_id");
				accid = Long.parseLong(String.valueOf(acc_attribute));
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				st = con.prepareStatement("select sent_to,passcode from invitations where for_account=? AND invitation_status=?;");
				st.setLong(1, accid);
				st.setString(2, "not joined");
				rs = st.executeQuery();
				while(rs.next())
				{
					sent_to = rs.getString("sent_to");
					passcode = rs.getString("passcode");
					jo.put("sent_to",sent_to);
					jo.put("passcode",passcode);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
