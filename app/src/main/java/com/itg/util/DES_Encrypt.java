package com.itg.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.rt.BASE64Decoder;

public class DES_Encrypt {
	public static String decryptDES(String decryptString, String decryptKey) throws Exception {
		byte[] byteMi = new BASE64Decoder().decodeBuffer(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData, "UTF-8");
	}

//	public static String decryptDES(String message, String key) throws Exception {
//		// base64 + des ����.net ���ܴ�����
//		// byte[] bytesrc = convertHexString(message); //����base64 �ķ�ʽ
//		BASE64Decoder base64Decoder = new BASE64Decoder();
//		byte[] bytesrc = base64Decoder.decodeBuffer(message);
//		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
//		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
//		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
//		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
//		byte[] retByte = cipher.doFinal(bytesrc);
//
//		return new String(retByte, "UTF-8");
//
//	}
}
