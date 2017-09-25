package com.budgetchummy.api.rest;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.emailUtil;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


@WebServlet(urlPatterns = {"/api/v1/addUser", "/BudgetChummy/api/v1/addUser"})
public class addUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public addUserServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession http_session = request.getSession(false);
		if(http_session.getAttribute("user_id") == null)
		{
			response.setStatus(400);
		}
		else
		{
			long userid=0,accid=0;
			boolean invitationAlreadySent = false;
			boolean isAdmin = false;
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			String to = request.getParameter("to");
			String authentication_type = request.getParameter("authentication_type");
			String passcode=null;
			long invitationid = 0;
			if(authentication_type.equals("Offline"))
			{
				passcode = request.getParameter("passcode");
			}
			else
			{
				Random ran = new Random();
				passcode = String.valueOf(100000 + ran.nextInt(900000));
			}
			String first_name=null;
					
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
				Object user_attribute = http_session.getAttribute("user_id");
				Object acc_attribute = http_session.getAttribute("account_id");
				userid = Long.parseLong(String.valueOf(user_attribute));
				accid = Long.parseLong(String.valueOf(acc_attribute));

				st = con.prepareStatement("select role from adduser where user_id=? AND account_id=?;");
				st.setLong(1, userid);
				st.setLong(2, accid);
				rs = st.executeQuery();
				if(rs.next())
				{
					String role = rs.getString("role");
					if(role.equals("admin"))
					{
						isAdmin = true;
					}
					else
					{
						response.setStatus(401);
					}
					rs = null;
				}

				if(isAdmin)
				{
					st = con.prepareStatement("select sent_by, sent_to, for_account, invitation_status from invitations where sent_to=? AND for_account=?;");
					st.setString(1, to);
					st.setLong(2, accid);
					rs = st.executeQuery();
					JSONArray ja = new JSONArray();
					JSONObject jo = new JSONObject();
					if(rs.next())
					{
						invitationAlreadySent = true;
						String inv_status = rs.getString("invitation_status");
						jo.put("sent_to", to);
						jo.put("invitation_status", inv_status);
						ja.add(jo.toJSONString());
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().print(ja.toString());
						rs = null;
					}
					else
					{
						st = con.prepareStatement("insert into invitations(sent_by,sent_to,passcode,invitation_status, for_account) values(?,?,?,?,?);");
						st.setLong(1, userid);
						st.setString(2, to);
						st.setString(3, passcode);
						st.setString(4, "not joined");
						st.setLong(5, accid);
						int i = st.executeUpdate();

						st = con.prepareStatement("select lastval();");
						rs = st.executeQuery();
						if(rs.next())
						{
							invitationid = rs.getInt(1);
						}

						st1 = con.prepareStatement("select first_name from users where user_id=?;");
						st1.setLong(1, userid);
						rs1 = st1.executeQuery();
						while(rs1.next())
						{
							first_name = rs1.getString("first_name");
						}
					}
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
			}catch (SQLException e) {
				e.printStackTrace();
			}	

			if(!invitationAlreadySent && isAdmin)
			{
				String rootURL = APIConstants.rootURL;
				String subject = "Invitation to join Budget Chummy";
				String message = "Hi "+to+"\n"+first_name+" has sent you an invitation to join his Budget Chummy account\n"+
		                          "Click the below link to join\n"+
		        		          rootURL + "BC?account_id="+accid+"&invitation_id="+invitationid+"\n";
	    		if(authentication_type.equals("Email"))
	          	{
	          		message += "Passcode : "+passcode;
	          	}
		                          
				emailUtil.sendMail(to, subject, message);
			}
		}
		
	}

}
