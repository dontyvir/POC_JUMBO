/*
 * Created on 07-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jdroguett
 * 
 * Filtra archivos JCV por fechas y locales
 */
public class Filtro implements FilenameFilter {
    private Date desde;

    private Date hasta;

    private String[] locales;

    /**
     * 
     * @param desde
     * @param hasta
     * @param locales
     */
    public Filtro(Date desde, Date hasta, String[] locales) {
        this.desde = desde;
        this.hasta = hasta;
        this.locales = locales;
    }

    public boolean accept(File dir, String name) {
        if (name.startsWith("JCV")) {
            String local = name.substring(3, 7);
            if (buscar(local)) {
                GregorianCalendar fecha = new GregorianCalendar();
                fecha.setTime(desde);
                while (fecha.getTimeInMillis() < hasta.getTime()) {
                    int mes = fecha.get(Calendar.MONTH) + 1;
                    int dia = fecha.get(Calendar.DAY_OF_MONTH);
                    String sMes = mes < 10 ? "0" + mes : "" + mes;
                    String sDia = dia < 10 ? "0" + dia : "" + dia;
                    Pattern p = Pattern.compile("JCVJ..." + sMes + sDia + "......");
                    Matcher m = p.matcher(name);
                    if (m.find()) {
                        return true;
                    }
                    fecha.add(Calendar.DAY_OF_YEAR, 1);
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