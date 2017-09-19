package com.budgetchummy.api.util;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	private static int workload = 12;
	private static String staticSalt = "iAmPhenomenal";
	
	private static String addStaticSalt(String orgPassword)
	{
		return orgPassword + staticSalt;
	}
	public static String generatePassword(String orgPassword)
	{
		String addedStaticSalt = addStaticSalt(orgPassword);
		String dynamicSalt = BCrypt.gensalt(workload);
		String generatedSecurePasswordHash = BCrypt.hashpw(addedStaticSalt, dynamicSalt);
		return generatedSecurePasswordHash;
	}
	public static boolean verifyPassword(String password, String hash)
	{
		boolean password_verified = false;
		String addedStaticSalt = addStaticSalt(password);
		password_verified = BCrypt.checkpw(addedStaticSalt, hash);
		return password_verified;
	}
}
