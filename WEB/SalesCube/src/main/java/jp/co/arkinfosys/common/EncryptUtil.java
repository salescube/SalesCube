/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.common;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * パスワード暗号化に関するユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class EncryptUtil {

	/**
	 * 128bitAES暗号化のためのデフォルト秘密鍵<br>
	 * キーは暗号する文字列自身を使ってさらに複雑化する
	 */
	private static final String DEFAULT_PRIVATE_KEY = "jp.co.arkinfosys";
	
	/**
	 * 暗号化方式 : AES
	 */
	private static final String ENCRYPT_AES = "AES";
	
	/**
	 * 暗号化方式 : MD5
	 */
	private static final String ENCRYPT_MD5 = "MD5";
	
	/**
	 * 指定した文字列に対して、設定された暗号化方式で暗号化した結果を返します.
	 * @param encSrc 暗号化する文字列
	 * @return 暗号化済み文字列
	 * @throws GeneralSecurityException
	 * @throws NoSuchAlgorithmException
	 */
	public static String encrypt(String encSrc) throws GeneralSecurityException, NoSuchAlgorithmException {
		
		String encryptStyle = (String) ConfigUtil.getConfigValue(ConfigUtil.KEY.PASSWORD_ENCRYPT_STYLE);
		
		
		if(encryptStyle == null || encryptStyle.length() == 0) {
			encryptStyle = ENCRYPT_AES;
		}
		
		
		String encryptString = null;
		
		if(ENCRYPT_MD5.equals(encryptStyle)) {
			
			encryptString = encryptMD5(encSrc);
			
		} else {
			
			encryptString = encryptAES(encSrc);
		}
		
		return encryptString;
	}

	/**
	 * 指定した文字列に対してAES128bit暗号アルゴリズムによる暗号化を行い、16進数表現の文字列として結果を返却します.
	 *
	 * @param encSrc 暗号化する文字列
	 * @return 暗号化済み文字列
	 * @throws GeneralSecurityException
	 */
	public static String encryptAES(String encSrc) throws GeneralSecurityException {
		
		byte[] keySrc = EncryptUtil.DEFAULT_PRIVATE_KEY.getBytes();
		byte[] userKeySrc = encSrc.getBytes();
		System.arraycopy(userKeySrc, 0, keySrc, 0, Math.min(keySrc.length,
				userKeySrc.length));
		Key key = new SecretKeySpec(keySrc, "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedPassword = cipher.doFinal(encSrc.getBytes());

		return asHex(encryptedPassword);
	}
	
	/**
	 * 指定した文字列に対してMD5ハッシュによる暗号化を行い、16進数表現の文字列として結果を返却します.
	 * @param encSrc 暗号化する文字列
	 * @return 暗号化済み文字列
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptMD5(String encSrc) throws NoSuchAlgorithmException {
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(encSrc.getBytes());
		byte[] encryptedHash = md5.digest();

		return asHex(encryptedHash);
	}

	/**
	 * 指定したバイト配列を16進数表記の文字列に変換します.
	 * @param bytes　変換するバイト配列
	 * @return 変換後の文字列
	 */
	public static String asHex(byte[] bytes) {
		StringBuffer buff = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			int bt = bytes[i] & 0xff;

			if (bt < 0x10) {
				buff.append("0");
			}

			buff.append(Integer.toHexString(bt));
		}

		
		return buff.toString();
	}

}
