/*
 * Created on 08-oct-2009
 */
package cl.cencosud.informes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author jdroguett
 */
public class TestPoi {
   public static void main(String[] args) {
      SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
      GregorianCalendar f = new GregorianCalendar();
      /*f.set(Calendar.DAY_OF_MONTH, 31);
      f.set(Calendar.MONTH,11);
      System.out.println(s.format(f.getTime()) + "    semana: " + f.get(Calendar.WEEK_OF_YEAR));
      */
      f.set(2009,11,30);
      
      
      for (int i = 0; i < 7; i++) {
         System.out.println(s.format(f.getTime()) + "    semana: " + f.get(Calendar.WEEK_OF_YEAR));
         f.add(Calendar.YEAR, 1);
      }
   }
}

