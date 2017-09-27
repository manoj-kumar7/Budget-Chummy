package com.budgetchummy.api.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class messageDigestUtil {

	private static String staticSalt = "iAmPhenomenal";

	public static String getMD5Hash(String input)
	{
		input = input + staticSalt;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) 
			{
                hashtext = "0" + hashtext;
            }
            return hashtext;
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
}
