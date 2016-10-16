package cl.cencosud.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtil {
	

	/**
	 * Construye a GregorianCalendar con el mes y dia.
	 * 
	 * @param mes 
	 * @param dia 
	 * 
	 * */
	public static GregorianCalendar getCalendar(int mes, int dia)throws Exception{
		GregorianCalendar hoy = new GregorianCalendar();	        
	    hoy.set(Calendar.MONTH, mes - 1);
	    hoy.set(Calendar.DAY_OF_MONTH, dia);
	    hoy.set(Calendar.HOUR_OF_DAY, 0);
	    hoy.set(Calendar.MINUTE, 0);
	    hoy.set(Calendar.SECOND, 0);
	    hoy.set(Calendar.MILLISECOND, 0);
		return hoy;		
	}
}
