package com.budgetchummy.api.util;

import org.quartz.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.*;

import com.budgetchummy.api.util.APIConstants;
import com.budgetchummy.api.util.emailUtil;
import com.budgetchummy.api.util.Datehelper;
import com.budgetchummy.api.util.JobsUtil;

public class ReminderSchedulerJob implements Job{
	
		public void sendReminderMail(String tag_name, String first_name, String reminder_type, String account_name, float amount, String date, String added_by, String description, String email){
			String rootURL = APIConstants.rootURL;
			String subject = "Budget Chummy - Reminder for " + tag_name;
			String email_welcome_html = "<div class='email-welcome-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>Hello " + first_name + "!</div>";
			String email_content_html = "<div class='email-content-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'>This is a reminder for the "+reminder_type+" added to the account "+ account_name +". Have a look at the "+ reminder_type +" details below.</div>";
			String email_overview_html = "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Account Name</div> : <div style='width:60%;float:right;'>"+account_name+ "</div></div>";
			email_overview_html += "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Amount</div> : <div style='width:60%;float:right;'>"+amount+ "</div></div>";
			email_overview_html += "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Date</div> : <div style='width:60%;float:right;'>"+date+ "</div></div>";
			email_overview_html += "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Tag Name</div> : <div style='width:60%;float:right;'>"+tag_name+ "</div></div>";
			email_overview_html += "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Added By</div> : <div style='width:60%;float:right;'>"+added_by+ "</div></div>";
			if(description != null && !description.equals(""))
			{
				email_overview_html += "<div class='email-overview-text' style='padding: 10px;font-size: 16px;font-family:\"Trebuchet MS\";'><div style='width:30%;float:left;'>Description</div> : <div style='width:60%;float:right;'>"+description+ "</div></div>";
			}
			String email_button_html = "<div style='margin: 0 auto;width: 20%;margin-top: 30px;margin-bottom: 30px;'><a href='"+rootURL+"' id='verify-email-btn' class='email-btn' style='display: inline-block;background-color: #f4511e;border: none;color: #FFFFFF;text-align: center;font-size: 17px;padding: 12px;width: 100%;font-family: \"Trebuchet MS\";letter-spacing: 1px;text-decoration: none;border-radius: 3px;cursor: pointer;'>TAKE ME TO BC</a></div>";
			String message = "<div class='email-box' style='width:70%;height:100%;background-color:#e2e2e2;margin:0 auto;'>"+email_welcome_html+
	 						 email_content_html+
							 "<div style='width:50%;margin-left:auto;margin-right:auto;'>" + email_overview_html + "</div>" +
							 email_button_html+ "</div>";
			emailUtil.sendMail(email, subject, message);
		}
		public void execute(JobExecutionContext arg0) throws JobExecutionException{
			String url = APIConstants.POSTGRESQL_URL;
			String user = APIConstants.POSTGRESQL_USERNAME;
			String mysql_password = APIConstants.POSTGRESQL_PASSWORD;

			long system_time = Datehelper.getServerTimeInEpoch();

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			try {
				Connection con = null;
				con = DriverManager.getConnection(url,user,mysql_password);
				PreparedStatement st=null;
				st = con.prepareStatement("select job_id,reminder_type,data_id,do_at from jobs where do_at<=?");
				st.setLong(1, system_time);
				ResultSet rs=null, rs1=null, rs2=null;
				rs = st.executeQuery();

				long job_id, data_id, do_at, account_id;
				float amount;
				String reminder_type, added_by, account_name, date, tag_name, description, first_name, email;
				emailUtil.createSession();
				emailUtil.createConnection();
				while(rs.next())
				{
					job_id=rs.getLong("job_id");
					data_id=rs.getLong("data_id");
					do_at=rs.getLong("do_at");
					reminder_type=rs.getString("reminder_type");
					st = con.prepareStatement("select users.first_name, accounts.account_id,accounts.account_name, date, amount, tags.tag_name, description from users,accounts,tags,transactions where transaction_id=? AND users.user_id=transactions.user_id AND accounts.account_id=transactions.account_id AND tags.tag_id=transactions.tag_id;");
					st.setLong(1, data_id);
					rs1 = st.executeQuery();
					if(rs1.next())
					{
						added_by = rs1.getString("first_name");
						account_id = rs1.getLong("account_id");
						account_name = rs1.getString("account_name");
						amount = rs1.getFloat("amount");
						date = Datehelper.epochToCustomFormat(rs1.getLong("date"));
						tag_name = rs1.getString("tag_name");
						description = rs1.getString("description");
						st = con.prepareStatement("select users.first_name,users.email from users,adduser where account_id=? AND users.user_id=adduser.user_id;");
						st.setLong(1, account_id);
						rs2 = st.executeQuery();
						while(rs2.next())
						{
							first_name = rs2.getString("first_name");
							email = rs2.getString("email");
							sendReminderMail(tag_name, first_name, reminder_type, account_name, amount, date, added_by, description, email);
						}
					}
					try
					{
						JobsUtil.deleteJob(data_id);
					}
					catch(IOException io){
						io.printStackTrace();
					}
				}
				emailUtil.closeConnection();
				if(rs != null)
				{
					rs.close();
				}
				if(rs1 != null)
				{
					rs1.close();
				}
				if(rs2 != null)
				{
					rs2.close();
				}
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
}
