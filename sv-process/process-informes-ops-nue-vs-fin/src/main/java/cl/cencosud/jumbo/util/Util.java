package cl.cencosud.jumbo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Util {
	
	private static String BUNDLE_NAME = "conf";
	
    public static String getPropertiesString(String key) {
        try {
            return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return '!' + key + '!';
        }
    }
	
	public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
        return formateador.format(ahora);
    }
	
	public static String getDiaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd");
        return formateador.format(ahora);
    }
	
	public static String getMesActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("MM");
        return formateador.format(ahora);
    }
	
	public static boolean validaFechaParam(String fechaSolicitada){
		 try {
			 SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			 Date fecha = formatoFecha.parse(fechaSolicitada);
		 } catch (Exception e) {
			 return false;
		 }
		 return true;
	}

}
