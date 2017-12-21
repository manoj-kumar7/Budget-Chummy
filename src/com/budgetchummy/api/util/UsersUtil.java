package com.budgetchummy.api.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.Datehelper;
import com.budgetchummy.api.util.emailUtil;

public class UsersUtil {
	
	public static void getUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
			PreparedStatement st=null,st1=null;
			ResultSet rs = null,rs1=null;		
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
				String query=null;
				String account_name=null,created_by=null,created_date_time=null;
				String role=null,first_name=null,email=null;
				long no_of_members=-1,created_by_id = -1,user_id=-1;
				Object acc_attribute = session.getAttribute("account_id");
				accid = Long.parseLong(String.valueOf(acc_attribute));
				JSONArray ja = new JSONArray();
				JSONObject jo = new JSONObject();
				st = con.prepareStatement("select account_name,created_by,users.first_name, no_of_members,accounts.created_date_time from users,accounts where users.user_id=accounts.created_by AND account_id=?;");
				st.setLong(1, accid);
				rs = st.executeQuery();
				while(rs.next())
				{
					account_name = rs.getString("account_name");
					created_by_id = rs.getLong("created_by");
					created_by = rs.getString("first_name");
					no_of_members = rs.getLong("no_of_members");
					created_date_time = Datehelper.epochToDate(rs.getLong("created_date_time"));
				}
				jo.put("account_name",account_name);
				jo.put("no_of_members",no_of_members);
				jo.put("created_by",created_by);
				jo.put("created_date_time",created_date_time);
				ja.add(jo.toJSONString());
				jo.clear();
				
				st = con.prepareStatement("select adduser.user_id,role,users.first_name,users.email from adduser,users where account_id=? AND users.user_id=adduser.user_id;");
				st.setLong(1, accid);
				rs = st.executeQuery();
				while(rs.next())
				{
					user_id = rs.getInt("user_id");
					role = rs.getString("role");
					first_name = rs.getString("first_name");
					email = rs.getString("email");
					jo.put("user_id", user_id);
					jo.put("first_name",first_name);
					jo.put("email",email);
					jo.put("role",role);
					ja.add(jo.toJSONString());
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
	
	public static void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			Connection con = null;
			PreparedStatement st=null,st1=null;
			ResultSet rs = null,rs1=null;		
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
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
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs != null)
				{
					try{
						rs.close();
					}catch (SQLException e) { /* ignored */}
				}
				if(rs1 != null)
				{
					try{
						rs1.close();
					}catch (SQLException e) { /* ignored */}
				}
				try{
					if(st != null)
					{
						st.close();
					}
				}catch (SQLException e) { /* ignored */}
				try{
					if(st1 != null)
					{
						st1.close();
					}
				}catch (SQLException e) { /* ignored */}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}

			if(!invitationAlreadySent && isAdmin)
			{
				String rootURL = APIConstants.rootURL;
				String finalURL = rootURL + "BC?account_id="+accid+"&invitation_id="+invitationid;
				String subject = "Invitation to join Budget Chummy";
				String email_welcome_html = "<div class='email-welcome-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>Hello " + to + "!</div>";
				String email_content_html = "<div class='email-content-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>"+first_name+" has sent you an invitation to join his Budget Chummy account</div>";
				String email_passcode_html = "";
				if(authentication_type.equals("Email"))
	          	{
	          		email_passcode_html = "<div class='email-passcode-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>Passcode : "+passcode+ "</div>";
	          	}
				String email_button_html = "<div style='margin: 0 auto;width: 20%;margin-top: 30px;margin-bottom: 30px;'><a href='"+finalURL+"' id='verify-email-btn' class='email-btn' style='display: inline-block;background-color: #f4511e;border: none;color: #FFFFFF;text-align: center;font-size: 17px;padding: 12px;width: 100%;font-family: \"Trebuchet MS\";letter-spacing: 1px;text-decoration: none;border-radius: 3px;cursor: pointer;'>JOIN ACCOUNT</a></div>";
				String message = "<div class='email-box' style='width:70%;height:100%;background-color:#e2e2e2;margin:0 auto;'>"+email_welcome_html+
									 email_content_html+
									 email_passcode_html+
									 email_button_html+ "</div>";
		  //       emailUtil.createSession();
				// emailUtil.createConnection();
				emailUtil.sendMail(to, subject, message);
				// emailUtil.closeConnection();
			}
		}
		
	}
	
	public static void joinAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);

		Object user_attribute = session.getAttribute("user_id");
		long userid = Long.parseLong(String.valueOf(user_attribute));
		String passcode = request.getParameter("passcode");
		long account_id = Long.parseLong(request.getParameter("account_id"));
		long invitation_id = Long.parseLong(request.getParameter("invitation_id"));
		String passcode_from_db = null,invitation_status=null;
		String query = null;
		boolean isValidURL = false;
		String logged_in_mail_id = null;

		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		Connection con = null;
		PreparedStatement st=null,st1=null;
		ResultSet rs=null;		
		try {
			con = DriverManager.getConnection(url,user,mysql_password);
			st = con.prepareStatement("select email from users where user_id=?;");
			st.setLong(1, userid);
			rs = st.executeQuery();
			if(rs.next())
			{
				logged_in_mail_id = rs.getString("email");
			}
			rs = null;

			st = con.prepareStatement("select passcode,invitation_status from invitations where invitation_id=? AND for_account=? AND sent_to=?");
			st.setLong(1, invitation_id);
			st.setLong(2, account_id);
			st.setString(3, logged_in_mail_id);
			rs = st.executeQuery();
			if(rs.next())
			{
				isValidURL = true;
				passcode_from_db = rs.getString("passcode");
				invitation_status = rs.getString("invitation_status");
				rs = null;
			}
			else
			{
				response.setStatus(402);
			}
			if(isValidURL)
			{
				if(invitation_status.equals("joined"))
				{
					
				}
				else if(passcode.equals(passcode_from_db))
				{
					st1 = con.prepareStatement("insert into adduser(account_id,user_id,role) values(?,?,?);");
					st1.setLong(1, account_id);
					st1.setLong(2, userid);
					st1.setString(3, "user");
					int i;
					i = st1.executeUpdate();
					st1 = con.prepareStatement("update accounts set no_of_members=no_of_members+1 where account_id=?;");
					st1.setLong(1, account_id);
					i = st1.executeUpdate();
					st1 = con.prepareStatement("update invitations set invitation_status='joined' where invitation_id=?;");
					st1.setLong(1, invitation_id);
					i = st1.executeUpdate();
					session.setAttribute("account_id",account_id);
				}
				else if(!passcode.equals(passcode_from_db))
				{
					response.setStatus(401);
				}				
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
				if(st != null)
				{
					st.close();
				}
			}catch (SQLException e) { /* ignored */}
			try{
				if(st1 != null)
				{
					st1.close();
				}
			}catch (SQLException e) { /* ignored */}
			try{
				con.close();
			}catch (SQLException e) { /* ignored */}
		}
		
	}
	
	public static void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if(session.getAttribute("user_id") == null)
		{
			response.setStatus(400);
		}
		else
		{
			Object user_attribute = session.getAttribute("user_id");
			long userid = Long.parseLong(String.valueOf(user_attribute));
			Object account_attribute = session.getAttribute("account_id");
			long accid = Long.parseLong(String.valueOf(account_attribute));
			long delete_user_id = Long.parseLong(request.getParameter("delete_user_id"));
			String delete_user_email=null;
			long no_of_members = 0;
			boolean isAdmin = false;

			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				out.println("driver not found");
			}
			Connection con = null;
			PreparedStatement st=null,st1=null;
			ResultSet rs=null;
			try {
				con = DriverManager.getConnection(url,user,mysql_password);
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
					st1 = con.prepareStatement("delete from adduser where account_id=? AND user_id=?;");
					st1.setLong(1, accid);
					st1.setLong(2, delete_user_id);
					int i = st1.executeUpdate();

					st1 = con.prepareStatement("update accounts set no_of_members=no_of_members-1 where account_id=?;");
					st1.setLong(1, accid);
					i = st1.executeUpdate();

					st1 = con.prepareStatement("select user_id, email from users where user_id=?;");
					st1.setLong(1, delete_user_id);
					rs = st1.executeQuery();
					if(rs.next())
					{
						delete_user_email = rs.getString("email");
					}
					rs=null;
					st1 = con.prepareStatement("delete from invitations where for_account=? AND sent_to=?;");
					st1.setLong(1, accid);
					st1.setString(2, delete_user_email);
					i = st1.executeUpdate();

					st1 = con.prepareStatement("select no_of_members from accounts where account_id=?;");
					st1.setLong(1, accid);
					rs = st1.executeQuery();
					if(rs.next())
					{
						no_of_members = rs.getInt("no_of_members");
					}
					rs = null;
					if(no_of_members <= 0)
					{
						st1 = con.prepareStatement("delete from accounts where account_id=?;");
						st1.setLong(1, accid);
						i = st1.executeUpdate();
						session.removeAttribute("account_id");
					}
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
					if(st != null)
					{
						st.close();
					}
				}catch (SQLException e) { /* ignored */}
				try{
					if(st1 != null)
					{
						st1.close();
					}
				}catch (SQLException e) { /* ignored */}
				try{
					con.close();
				}catch (SQLException e) { /* ignored */}
			}
		}
		
	}
}
