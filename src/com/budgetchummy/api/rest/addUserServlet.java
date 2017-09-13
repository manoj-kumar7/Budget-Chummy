package com.budgetchummy.api.rest;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


@WebServlet(urlPatterns = {"/addUser", "/BudgetChummy/addUser"})
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
		long userid=0,accid=0;
		
		String url = APIConstants.POSTGRESQL_URL;
		String user = APIConstants.POSTGRESQL_USERNAME;
		String mysql_password = APIConstants.POSTGRESQL_PASSWORD;
		String to = request.getParameter("add-user-input");
		String authentication_type = request.getParameter("authentication_type");
		String passcode=null;
		long invitationid = 0;
		if(authentication_type.equals("Offline"))
		{
			passcode = request.getParameter("offline-code");
		}
		else
		{
			Random ran = new Random();
			passcode = String.valueOf(100000 + ran.nextInt(900000));
		}
		String from = "manoj.budgetchummy@gmail.com";
		String pass="Manoj@bc1";
		String host = "localhost";
		String first_name=null;
		
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		//properties.setProperty("mail.password", "Manoj@bc1");
	    Session session = Session.getInstance(properties,new javax.mail.Authenticator()
	    {
	  	  protected PasswordAuthentication getPasswordAuthentication() 
	  	  {
	  	 	 return new PasswordAuthentication("manoj.budgetchummy@gmail.com","Manoj@bc1");
	  	  }
	   });		
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			out.println("driver not found");
		}
		
		try {
			Connection con = null;
			con = DriverManager.getConnection(url,user,mysql_password);
			Statement st=null,st1=null;
			st = con.createStatement();
			st1 = con.createStatement();
			ResultSet rs = null,rs1=null;
			String query=null;
			HttpSession http_session = request.getSession();
			if(http_session == null)
			{
				response.sendRedirect("login.jsp");
			}
			Object user_attribute = http_session.getAttribute("user_id");
			Object acc_attribute = http_session.getAttribute("account_id");
			userid = Long.parseLong(String.valueOf(user_attribute));
			accid = Long.parseLong(String.valueOf(acc_attribute));
			query = "insert into invitations(sent_by,sent_to,passcode,invitation_status) values("+userid+",'"+to+"','"+passcode+"','"+"not joined"+"');";
			st.executeUpdate(query);
			query = "select lastval();";
			rs = st.executeQuery(query);
			if(rs.next())
			{
				invitationid = rs.getInt(1);
			}
			query = "select first_name from users where user_id="+userid+";";
			rs1 = st1.executeQuery(query);
			while(rs1.next())
			{
				first_name = rs1.getString("first_name");
			}
			rs.close();
			st.close();
			rs1.close();
			st1.close();
			con.close();	
		}catch (SQLException e) {
			e.printStackTrace();
		}	
	      try{
	          // Create a default MimeMessage object.
	          MimeMessage message = new MimeMessage(session);
	          // Set From: header field of the header.
	          message.setFrom(new InternetAddress(from));
	          // Set To: header field of the header.
	          message.addRecipient(Message.RecipientType.TO,
	                                   new InternetAddress(to));
	          // Set Subject: header field
	          message.setSubject("Invitation to join Budget Chummy");
	          // Now set the actual message
	          if(authentication_type.equals("Email"))
	          {
		          message.setText("Hi "+to+"\n"+first_name+" has sent you an invitation to join his Budget Chummy account\n"+
                          "Click the below link to join\n"+
        		          "FirstPage.jsp?account_id="+accid+"&invitation_id="+invitationid+"\n"+
                          "Passcode : "+passcode);	        	  
	          }
	          else
	          {
		          message.setText("Hi "+to+"\n"+first_name+" has sent you an invitation to join his Budget Chummy account\n"+
                          "Click the below link to join\n"+
        		          "FirstPage.jsp?account_id="+accid+"&invitation_id="+invitationid);	        	  
	          }

	          // Send message
	          Transport.send(message);
	          
	       }catch (MessagingException mex) {
	          mex.printStackTrace();
	       }
	      

	}

}
