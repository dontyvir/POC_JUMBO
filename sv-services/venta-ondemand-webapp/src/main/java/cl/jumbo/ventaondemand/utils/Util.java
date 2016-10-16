package cl.jumbo.ventaondemand.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;
import cl.jumbo.ventaondemand.exceptions.OnDemandException;

public class Util {
	
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();	
		
	
	/**
	 * Encripta en MD5
	 * 
	 * @param texto 	Texto a encriptar
	 * @return 			texto encriptado
	 * @throws SystemException
	 */
	public static String encriptarFO( String texto ) throws OnDemandException {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new OnDemandException("Problemas con encriptacion clave", e);
		}
		
		md.update(texto.getBytes());
		
		byte raw[] = md.digest(); // Obtención del resumen de mensaje
		String hash = (new BASE64Encoder()).encode(raw); // Traducción a BASE64
		return hash;		
				
	}	
	
	public static boolean isPostMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "POST");
	}

	public static long redondear( double valor ) {
		return Math.round(valor);
	}
	
	public static String printNombre(String nombre) {
        nombre = nombre.replaceAll("Ñ","&Ntilde;");
        nombre = nombre.replaceAll("ñ","&ntilde;");
        nombre = nombre.replaceAll("Á","&Aacute;");
        nombre = nombre.replaceAll("á","&aacute;");
        nombre = nombre.replaceAll("É","&Eacute;");
        nombre = nombre.replaceAll("é","&eacute;");
        nombre = nombre.replaceAll("Í","&Iacute;");
        nombre = nombre.replaceAll("í","&iacute;");
        nombre = nombre.replaceAll("Ó","&Oacute;");
        nombre = nombre.replaceAll("ó","&oacute;");
        nombre = nombre.replaceAll("Ú","&Uacute;");
        nombre = nombre.replaceAll("ú","&uacute;");
        return nombre;
    }
	
	public static String randomString( int len ){
		StringBuffer sb = new StringBuffer( len );
		for( int i = 0; i < len; i++ ){
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		}
		//sb.append(sdff.format(new Date()));
		sb.append(System.currentTimeMillis());
		return sb.toString();
	}

}
