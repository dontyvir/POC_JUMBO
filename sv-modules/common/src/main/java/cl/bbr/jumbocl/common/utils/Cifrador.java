package cl.bbr.jumbocl.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Encoder;

/**
 * Clase cuyos métodos permiten encriptar y desencriptar claves
 * @author bbr
 *
 */
public class Cifrador {
	private static byte[] SALT_BYTES = {
	(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	(byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
					};
	private static int ITERATION_COUNT = 19;
    
	/**
	 * método que permite encriptar alguna clave
	 * @param  passPhrase
	 * @param  str
	 * @return String
	 * @throws Exception
	 */
	public static String encriptar(String passPhrase, String str) throws Exception {
		Cipher ecipher = null;
		Cipher dcipher = null;

		try {
			// Crear la key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT_BYTES, ITERATION_COUNT);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance("PBEWithMD5AndDES");
			dcipher = Cipher.getInstance("PBEWithMD5AndDES");
    
			// Preparar los parametros para los ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);
    
			// Crear los ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (javax.crypto.NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("Excepción al encriptar",e);
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Excepción al encriptar",e);
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception("Excepción al encriptar",e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("Excepción al encriptar",e);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw new Exception("Excepción al encriptar",e);
		}
		
		try {
			// Encodear la cadena a bytes usando utf-8
			byte[] utf8 = str.getBytes("UTF8");
    
			// Encriptar
			byte[] enc = ecipher.doFinal(utf8);
    
			// Encodear bytes a base64 para obtener cadena
			return new sun.misc.BASE64Encoder().encode(enc);
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		return null;
	}
    
	/**
	 * método que permite desencriptar una clave
	 * @param  passPhrase
	 * @param  str
	 * @return String
	 * @throws Exception
	 */
	public static String desencriptar(String passPhrase, String str) throws Exception {
		Cipher ecipher = null;
		Cipher dcipher = null;
    
		try {
			// Crear la key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT_BYTES, ITERATION_COUNT);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance("PBEWithMD5AndDES");
			dcipher = Cipher.getInstance("PBEWithMD5AndDES");
    
			// Preparar los parametros para los ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT_BYTES, ITERATION_COUNT);
    
			// Crear los ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);    
		} catch (javax.crypto.NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		}
		
		try {
			// Decodear base64 y obtener bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
    
			// Desencriptar
			byte[] utf8 = dcipher.doFinal(dec);
    
			// Decodear usando utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		} catch (java.io.IOException e) {
			e.printStackTrace();
			throw new Exception("Excepción al desencriptar",e);
		}

	}
	
	
	/**
	 * método de encriptación MD5
	 * @param  texto
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String toMD5( String texto ) throws NoSuchAlgorithmException {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new NoSuchAlgorithmException("Problemas con encriptacion clave");
		}
		md.update(texto.getBytes());
		
		byte raw[] = md.digest(); // Obtención del resumen de mensaje
		String hash = (new BASE64Encoder()).encode(raw); // Traducción a BASE64
		return hash;		
				
	}

}

