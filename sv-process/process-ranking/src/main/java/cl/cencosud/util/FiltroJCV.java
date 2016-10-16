/*
 * Created on 20-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jdroguett
 * 
 * Entrega archivo JCV que con la fecha indicada
 *  
 */
public class FiltroJCV implements FilenameFilter {
    private GregorianCalendar fecha;

    private String[] locales;

    public FiltroJCV(GregorianCalendar fecha, String[] locales) {
        this.fecha = fecha;
        this.locales = locales;
    }

    public boolean accept(File dir, String name) {
        if (name.startsWith("JCV")) {
            String local = name.substring(3, 7);
            if (buscar(local)) {
                int mes = fecha.get(Calendar.MONTH) + 1;
                int dia = fecha.get(Calendar.DAY_OF_MONTH);
                String sMes = mes < 10 ? "0" + mes : "" + mes;
                String sDia = dia < 10 ? "0" + dia : "" + dia;
                Pattern p = Pattern.compile("JCVJ..." + sMes + sDia + "......");
                Matcher m = p.matcher(name);
                if (m.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean buscar(String local) {
        for (int i = 0; i < locales.length; i++) {
            if (locales[i].equals(local))
                return true;
        }
        return false;
    }
}
