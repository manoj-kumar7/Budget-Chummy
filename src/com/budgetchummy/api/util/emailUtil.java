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
			
	public static void sendMail(String to, String subject, String messageBody)
	{
		Properties properties = System.getProperties();
//		properties.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.host", host);
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.user", emailId);
		properties.setProperty("mail.password", password);
		if(APIConstants.isProduction)
		{
//			properties.put("mail.smtp.starttls.enable", "true");
//			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			properties.put("mail.smtp.socketFactory.port", port);
//			properties.put("mail.smtp.port", port);
		}
		else
		{
			properties.put("mail.smtp.starttls.enable", "true");
//			properties.put("mail.smtp.port", port);
		}

		Session session = Session.getInstance(properties,new javax.mail.Authenticator()
	    {
	  	  	protected PasswordAuthentication getPasswordAuthentication() 
	  	  	{
	 			return new PasswordAuthentication(emailId, password);
	  	  	}
	   	});	

	   	try{
//	   		Session session = Session.getDefaultInstance(properties, null);
//	   		session.setDebug(true);
	   		Transport transport = session.getTransport("smtp");
 		 	MimeMessage message = new MimeMessage(session);
          	message.setFrom(new InternetAddress(emailId));
          	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          	message.setSubject(subject);
          	message.setContent(messageBody, "text/html");	   
          	transport.connect(host, emailId, password);
          	transport.send(message);
          	transport.close();
	          
        }catch (MessagingException mex) {
          	mex.printStackTrace();
        }

	}
}	
