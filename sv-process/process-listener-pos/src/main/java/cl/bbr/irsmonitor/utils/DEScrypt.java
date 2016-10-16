package cl.bbr.irsmonitor.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DEScrypt {

	public static byte[] encrypt( String text ) {
		try {
			//Security.addProvider( new com.sun.crypto.provider.SunJCE() );
			
			SecretKey key = new SecretKeySpec( "12345678".getBytes(), "DES" );
			
			byte[] plaintext = text.getBytes();
			//Cipher ecipher = Cipher.getInstance("DES");
			Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");//PKCS5Padding
			
			ecipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] eciphertext = ecipher.doFinal(plaintext);
			//System.out.println( "(enc-hex) " + toHex(eciphertext) );
			//System.out.println( "(enc-str) " + new String(eciphertext) );
			//System.out.println( "(enc-len) " + eciphertext.length );

			return eciphertext;
			
		} catch (NoSuchAlgorithmException e) {e.printStackTrace(); }
		catch (NoSuchPaddingException e) { e.printStackTrace(); }
		catch (InvalidKeyException e) { e.printStackTrace(); }
		catch (IllegalBlockSizeException e) { e.printStackTrace(); }
		catch (BadPaddingException e) { e.printStackTrace(); }
		
		return null;
	}
	
	public static byte[] decrypt( byte[] eciphertext ) {
		try {
			//Security.addProvider( new com.sun.crypto.provider.SunJCE() );
			SecretKey key = new SecretKeySpec( "12345678".getBytes(), "DES" );

			//Cipher dcipher = Cipher.getInstance("DES");
			Cipher dcipher = Cipher.getInstance("DES/ECB/NoPadding");//PKCS5Padding
			dcipher.init(Cipher.DECRYPT_MODE,key);
			byte[] dciphertext = dcipher.doFinal(eciphertext);
			System.out.println( "(dec-str) " + new String(dciphertext) );
			System.out.println( "(dec-len) " + dciphertext.length );
			
			return dciphertext;
			
		} catch (NoSuchAlgorithmException e) {e.printStackTrace(); }
		catch (NoSuchPaddingException e) { e.printStackTrace(); }
		catch (InvalidKeyException e) { e.printStackTrace(); }
		catch (IllegalBlockSizeException e) { e.printStackTrace(); }
		catch (BadPaddingException e) { e.printStackTrace(); }
		
		return null;
	}

	public static byte[] decryptBase64( String text ) throws IOException {
		return decrypt( new sun.misc.BASE64Decoder().decodeBuffer(text) );
	}

	public static String encryptBase64( String text ) {
		return new sun.misc.BASE64Encoder().encode( encrypt(text) );
	}
	
	public static String toHex(byte[] bytes) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) <= 0xf)
				s.append("0");
			s.append(Integer.toHexString(bytes[i] & 0xff).toUpperCase());
			s.append(" ");
		}
		
		return s.toString();
	}
	
	
	
}
