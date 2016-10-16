/*
 * Created on 31-ago-2009
 */
package cl.cencosud.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author jdroguett
 *
 */
public class Fechas {
   public static String getDiaMes(Date fecha){
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
      return sdf.format(fecha);
   }
   public static String getSemana(Date f) {
      return getSemana(new java.sql.Date(f.getTime()));
      
   }
   /**
    * Retorna fechas de lunes y domingo
    * @param f
    * @return
    */
   public static String getSemana(java.sql.Date f) {
      GregorianCalendar fecha = new GregorianCalendar();
      fecha.setTimeInMillis(f.getTime());
      StringBuffer sSemana = new StringBuffer();
      fecha.setFirstDayOfWeek(Calendar.MONDAY);
      fecha.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      int dia = fecha.get(Calendar.DAY_OF_MONTH);
      int mes = fecha.get(Calendar.MONTH) + 1;
      sSemana.append(dia < 10 ? "0" + dia + "/" : dia + "/");
      sSemana.append(mes < 10 ? "0" + mes + " - " : mes + " - ");
      fecha.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
      dia = fecha.get(Calendar.DAY_OF_MONTH);
      mes = fecha.get(Calendar.MONTH) + 1;
      sSemana.append(dia < 10 ? "0" + dia + "/" : dia + "/");
      sSemana.append(mes < 10 ? "0" + mes : mes + "");
      return sSemana.toString();
   }


   /**
    * Retorna fechas de lunes y domingo
    * @param nSemanaDb
    * @return
    */
   public static String getSemana(int nSemanaDb) {
      StringBuffer sSemana = new StringBuffer();
      GregorianCalendar fecha = new GregorianCalendar();
      fecha.setFirstDayOfWeek(Calendar.MONDAY);
      fecha.set(Calendar.WEEK_OF_YEAR, nSemanaDb);
      fecha.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
      int dia = fecha.get(Calendar.DAY_OF_MONTH);
      int mes = fecha.get(Calendar.MONTH) + 1;
      sSemana.append(dia < 10 ? "0" + dia + "/" : dia + "/");
      sSemana.append(mes < 10 ? "0" + mes + " - " : mes + " - ");
      fecha.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
      dia = fecha.get(Calendar.DAY_OF_MONTH);
      mes = fecha.get(Calendar.MONTH) + 1;
      sSemana.append(dia < 10 ? "0" + dia + "/" : dia + "/");
      sSemana.append(mes < 10 ? "0" + mes : mes + "");
      return sSemana.toString();
   }
}
