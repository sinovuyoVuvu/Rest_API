package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebElement;

public class MaskUtils {

	
	
	/**
	 * Execute mask string method
	 * @param args
	 * @author Craig Adam - Modified 26-02-2020
	 */
	public static void main(String[] args) {
		
		String stringToMask = MaskUtils.maskString();
		System.out.println(stringToMask);
		System.out.println(MaskUtils.unmaskString(stringToMask));

	}
	
	/**
	 * Populate field with unmasked string and click proceed button
	 * 
	 * @param MaskedPassword
	 * @param PasswordField
	 * @param ProceedButton
	 * @author Craig Adam - Modified 26-02-2020
	 * 
	 */
	public static void proceedLogin(String MaskedPassword, WebElement PasswordField, WebElement ProceedButton) {

		// Craig Adam
		PasswordField.sendKeys(unmaskString(MaskedPassword.trim()));
		ProceedButton.click();

	}

	/**
	 * Prompts for a string to mask and prints masked value to console
	 * 
	 * @author Craig Adam - Modified 26-02-2020
	 * 
	 */
	public static String maskString() {
			
		String skp = "\\\\srv006552\\Git_Repo\\.m2\\repository\\msk-sk\\flat";
				
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Input string to mask :");
		String msk = sc.next();

		try {
			
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(new String(Files.readAllBytes(Paths.get(skp))).split(",")[0].toCharArray(), new String(Files.readAllBytes(Paths.get(skp))).split(",")[1].getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec sk = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			ci.init(Cipher.ENCRYPT_MODE, sk, ivspec);
			return java.util.Base64.getEncoder().encodeToString(ci.doFinal(msk.getBytes("UTF-8")));
			
		} catch (Exception e) {
			
			System.out.println("Error while masking string: " + e.toString());
			
		}
		
		return null;
		
	}

	/**
	 * Unmask a masked string
	 * 
	 * @param MaskedString
	 * @return unmasked password
	 * @author Craig Adam - Modified 26-02-2020
	 * 
	 */
	public static String unmaskString(String MaskedString) {

		String skp = "\\\\srv006552\\Git_Repo\\.m2\\repository\\msk-sk\\flat";
		
		try {

			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(new String(Files.readAllBytes(Paths.get(skp))).split(",")[0].toCharArray(), new String(Files.readAllBytes(Paths.get(skp))).split(",")[1].getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec sk = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			ci.init(Cipher.DECRYPT_MODE, sk, ivspec);
			return new String(ci.doFinal(Base64.decodeBase64(MaskedString)));

		} catch (Exception e) {

			System.out.println("Error while unmasking sting: " + e.toString());

		}
		
		return null;
	}

}
