package cl.cencosud.procesos.carroabandonado.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

public class Util {
	/**
	 * Constante con el nombre de la configuración del properties
	 */
	private static String BUNDLE_NAME = "conf";
	
	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(Util.class.getName());

	
    /**
     * Recupera algun valor del properties.
     * 
     * @param key llave del properties
     * @return <code>String</code> con el valor de la propiedad
     */
    public static String getPropertiesString(String key) {
        try {
        	
        	logger.debug("[Util][getPropertiesString] recupero el valor del properties para la key " + key);
            return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
        
        } catch (MissingResourceException e) {
        
        	logger.error("[Util][getPropertiesString] Error al recuperar el valor",e);
            return '!' + key + '!';
        
        }
    }
	
	/**+
	 * Recupera la fecha actual con el formato yyyyMMdd
	 * @return <code>String</code> String con la fecha
	 */
	public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
        return formateador.format(ahora);
    }
	
	/**
	 * Método que se encarga de obtener la fecha y hora actuales
	 * @return <code>String</code> con la fecha actual con el formato dd-MM-yyyy HH:mm:ss
	 */
	public static String getFechaHoraActual(){
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return format.format(now);
	}
	
	/**
	 * Implementación publica para obtener la diferencia de tiempos entre dos String de fechas con formato dd-MM-yyyy HH:mm:ss
	 * 
	 * @param startDate   fecha de inicio
	 * @param endDate	  fecha de fin
	 * @return <code>String</code> tiempo de diferencia en dias horas:minutos:segundos
	 * @throws ParseException en caso de error de parse
	 */
	public static String getDateFromMsec(String startDate, String endDate) {
		try {
			SimpleDateFormat startFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			SimpleDateFormat endFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date start = startFormat.parse(startDate);
			Date end = endFormat.parse(endDate);

			return Util.getDateFromMsec(end.getTime() - start.getTime());
			
		} catch (Exception e) {
			
			return "0";
			
		}
	}
	
	/**
	 * Método que se encarga de indicar el tiempo de diferencia entre dos date
	 * @param diffMSec tiempo de diferencia entre time
	 * @return <code>String</code> tiempo de diferencia en dias horas:minutos:segundos
	 */
	private static String getDateFromMsec(long diffMSec) {
	    int left = 0;
	    int ss = 0;
	    int mm = 0;
	    int hh = 0;
	    int dd = 0;
	    left = (int) (diffMSec / 1000);
	    ss = left % 60;
	    left = (int) left / 60;
	    if (left > 0) {
	        mm = left % 60;
	        left = (int) left / 60;
	        if (left > 0) {
	            hh = left % 24;
	            left = (int) left / 24;
	            if (left > 0) {
	                dd = left;
	            }
	        }
	    }
	    String diff = "";
	    diff = diff + Integer.toString(dd) + " days ";
	    diff = diff + Integer.toString(hh) + ":";
	    
	    diff = diff + Integer.toString(mm) + ":" + Integer.toString(ss);

	    return diff;
	}
	
	/**
	 * Recupera el dia actual
	 * @return <code>String</code> el día con el formato DD
	 */
	public static String getDiaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd");
        return formateador.format(ahora);
    }
	
	/**
	 * Método que se encarga de recuperar el mes actual
	 * @return <code>String</code> con el mes actual con el formato MM
	 */
	public static String getMesActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("MM");
        return formateador.format(ahora);
    }

	public static String b64encode(byte[] b) throws MessagingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream b64os = MimeUtility.encode(baos, "base64");
		b64os.write(b);
		b64os.close();
		return new String(baos.toByteArray());
	}
	
	public static byte[] b64decode(String s) throws MessagingException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		InputStream b64is = MimeUtility.decode(bais, "Base64");
		byte[] tmp = new byte[s.length()];
		int n = b64is.read(tmp);
		byte[] res = new byte[n];
		System.arraycopy(tmp, 0, res, 0, n);
		return res;
	}
	
	public static String fixCaracteres(String s){
		String retorno="";
		
		retorno = s.replaceAll(" ", "_");
		retorno = retorno.replaceAll("á", "a");
		retorno = retorno.replaceAll("é", "e");
		retorno = retorno.replaceAll("í", "i");
		retorno = retorno.replaceAll("ó", "o");
		retorno = retorno.replaceAll("ú", "u");
		
		
		return retorno;
	}
	
}
