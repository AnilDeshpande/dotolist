package com.codetutor.dotolist.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AppUtil {

	public static String hashPassword(String password) {
		password = password + "TODOLISTAPP!234%632312";
		MessageDigest mdSha1 = null;
		try {
			mdSha1 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		try {
			mdSha1.update(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		byte[] data = mdSha1.digest();

		// String text = new String(data, "UTF-8");
		//password = Base64.encodeToString(data, Base64.NO_WRAP);
		password= Base64.getEncoder().encodeToString(data);

		return password;
	}
}
