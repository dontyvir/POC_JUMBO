package cl.jumbo.ventamasiva.utils;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
//import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

//import sun.misc.BASE64Encoder;
import cl.bbr.jumbocl.common.exceptions.SystemException;

public class Util {
	
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	//private static SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
		
	/**
	 * Encripta en MD5
	 * 
	 * @param texto 	Texto a encriptar
	 * @return 			texto encriptado
	 * @throws SystemException
	 */
	/*
	public static String encriptarFO( String texto ) throws SystemException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SystemException("Problemas con encriptacion clave", e);
		}
		md.update(texto.getBytes());
		byte raw[] = md.digest(); // Obtenci�n del resumen de mensaje
		String hash = (new BASE64Encoder()).encode(raw); // Traducci�n a BASE64
		return hash;
	}
	*/
	public static boolean isPostMethod(String method){
		return StringUtils.equals(StringUtils.upperCase(method), "POST");
	}

	public static long redondearFO( double valor ) {
		return Math.round(valor);
	}
	
	public static String printNombreFO(String nombre) {
        nombre = nombre.replaceAll("�","&Ntilde;");
        nombre = nombre.replaceAll("�","&ntilde;");
        nombre = nombre.replaceAll("�","&Aacute;");
        nombre = nombre.replaceAll("�","&aacute;");
        nombre = nombre.replaceAll("�","&Eacute;");
        nombre = nombre.replaceAll("�","&eacute;");
        nombre = nombre.replaceAll("�","&Iacute;");
        nombre = nombre.replaceAll("�","&iacute;");
        nombre = nombre.replaceAll("�","&Oacute;");
        nombre = nombre.replaceAll("�","&oacute;");
        nombre = nombre.replaceAll("�","&Uacute;");
        nombre = nombre.replaceAll("�","&uacute;");
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
