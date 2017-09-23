package com.budgetchummy.api.util;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;  

import com.budgetchummy.api.util.APIConstants;

public class emailUtil {

	private static String emailId = "manoj.budgetchummy@yahoo.com";
	private static String password = "Manoj@bc1";

	public static void sendMail(String to, String subject, String messageBody)
	{
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
		properties.put("mail.smtp.auth", "true");
		if(APIConstants.isProduction)
		{
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.port", "587");
			properties.put("mail.smtp.port", "587");
		}
		else
		{
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.port", "587");
		}

		Session session = Session.getInstance(properties,new javax.mail.Authenticator()
	    {
	  	  	protected PasswordAuthentication getPasswordAuthentication() 
	  	  	{
	 			return new PasswordAuthentication(emailId, password);
	  	  	}
	   	});	
		//session.setDebug(true);

	   	try{
 		 	MimeMessage message = new MimeMessage(session);
          	message.setFrom(new InternetAddress(emailId));
          	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          	message.setSubject(subject);
          	message.setText(messageBody);	        	  
          	Transport.send(message);
	          
        }catch (MessagingException mex) {
          	mex.printStackTrace();
        }

	}
}	
