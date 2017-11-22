package com.budgetchummy.api.util;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;  

import com.budgetchummy.api.util.APIConstants;

public class emailUtil {

	private static String emailId = APIConstants.EMAIL;
	private static String password = APIConstants.EMAIL_PASSWORD;
	private static String host = "smtpout.asia.secureserver.net";
	private static int port = 465;
	private static Session session;
	private static Transport transport;
	public static void createSession()
	{
		Properties properties = System.getProperties();
//		properties.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.user", emailId);
		properties.setProperty("mail.password", password);
		session = Session.getInstance(properties,new javax.mail.Authenticator()
	    {
	  	  	protected PasswordAuthentication getPasswordAuthentication() 
	  	  	{
	 			return new PasswordAuthentication(emailId, password);
	  	  	}
	   	});	
	}
	public static void createConnection()
	{
		try
		{
			Transport transport = session.getTransport("smtp");
	   		transport.connect(host, emailId, password);
		}
		catch (MessagingException mex) 
		{
	      	mex.printStackTrace();
	    }
	}
	public static void closeConnection()
	{
		try
		{
			transport.close();
		}
		catch(MessagingException mex)
		{
			mex.printStackTrace();
		}
	}
	public static void sendMail(String to, String subject, String messageBody)
	{
	   	try
	   	{
//	   		session.setDebug(true);
 		 	MimeMessage message = new MimeMessage(session);
          	message.setFrom(new InternetAddress(emailId));
          	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          	message.setSubject(subject);
          	message.setContent(messageBody, "text/html");	   
          	transport.send(message);
        }
	   	catch (MessagingException mex) 
	   	{
          	mex.printStackTrace();
        }
	}
}	
