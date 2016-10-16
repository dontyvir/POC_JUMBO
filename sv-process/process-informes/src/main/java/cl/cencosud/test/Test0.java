/*
 * Created on 28-dic-2009
 */
package cl.cencosud.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author jdroguett
 */
public class Test0 {

   public static void main(String[] args) {
      GregorianCalendar fecha = new GregorianCalendar(2006,00, 01);
      fecha.setFirstDayOfWeek(Calendar.MONDAY);
      for (int i = 0; i < 30; i++) {
         System.out.println("fecha: " + fecha.getTime() + "     semana: " + fecha.get(Calendar.WEEK_OF_YEAR));
         fecha.add(Calendar.DAY_OF_YEAR, 1);
      }
   }
}
