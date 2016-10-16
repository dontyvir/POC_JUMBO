/*
 * Creado el 20-sep-2011
 * 
 */
package cl.cencosud.enviotasa.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author imoyano
 * 
 */
public class Util {
    
    public static String printNroTarjeta(String nro) {
        if ( nro == null ) {
            return "";
        }
        if ( nro.length() > 4 ) {
            nro = nro.substring(nro.length()-4,nro.length());
        }
        return "**** **** **** " + nro;
    }
    
    public static String formatoNumero( double num ) {        
        Locale lidioma = new Locale("cl", "CL");
        NumberFormat df = NumberFormat.getInstance(lidioma);
        df.setMaximumFractionDigits(0);     
        return df.format( num ).replaceAll(",",".");
    }
    
    /**
     * De un formato yyyy-mm-dd lo deja asi dd-mm-yyyy
     * 
     * @param fecha
     * @return
     */
    public static String fechaOrdena(String fecha) {
        if ( fecha != null && !fecha.equals("") && fecha.length() == 10 ) {
            return fecha.substring(8,10)+"-"+fecha.substring(5,7)+"-"+fecha.substring(0,4);
        }
        return "";
    }
    
    /**
     * De un formato yymmdd lo deja asi dd-mm-yyyy
     * 
     * @param fecha
     * @return
     */
    public static String fechaOrdenaYAgrega(String fecha) {
        if ( fecha != null && !fecha.equals("") && fecha.length() == 6 ) {
            return fecha.substring(4,6)+"-"+fecha.substring(2,4)+"-20"+fecha.substring(0,2);
        }
        return "";
    }
    
    /**
     * De un formato yyyymmdd lo deja asi dd-mm-yyyy
     * 
     * @param fecha
     * @return
     */
    public static String fechaAgregaSeparador(String fecha) {
        if ( fecha != null && !fecha.equals("") && fecha.length() == 8 ) {
            return fecha.substring(6,8)+"-"+fecha.substring(4,6)+"-"+fecha.substring(0,4);
        }
        return "";
    }
    
    /**
     * Imprimir el error stack trace
     * 
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t)    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    
    /**
     * Capitalizar un string... usado en el nombre del cliente
     * 
     * @param string
     * @return
     */
    public static String capitalizeString(String string) {
        if ( string == null )
            return "";
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
          if (!found && Character.isLetter(chars[i])) {
            chars[i] = Character.toUpperCase(chars[i]);
            found = true;
          } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
            found = false;
          }
        }
        return String.valueOf(chars);
      }

    /**
     * Fecha y hora actual
     * 
     * @return
     */
    public static String fecha() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String s = dateFormat.format(date);
        return s;
    }
    
    public static String fechaArchivo(String patron, int diasMas) {
        SimpleDateFormat sdf = new SimpleDateFormat(patron);
        Calendar c1 = Calendar.getInstance(); 
        c1.add(Calendar.DATE,diasMas);
        return sdf.format(c1.getTime());    
    }

    /**
     * @param cuotas
     * @return
     */
    public static boolean sinCuota(String cuotas) {
        if ( cuotas == null ) {
            return true;
        }
        if ( Integer.parseInt(cuotas) == 0 ) {
            return true;
        }
        return false;
    }
    
    public static String calculaDigitoRut(String rut) {
        int largo = rut.length();
        int dig = 0;
        int cont = 2;
        int suma = 0;
        int result = 0;
        try {
            for (int pos = largo; pos > 0; pos--) {
                dig = Integer.parseInt(String.valueOf(rut.charAt(pos - 1)));
                suma = suma + (dig * cont);
                cont++;
                if (cont == 8)
                    cont = 2;
            }
            result = suma % 11;
            result = 11 - result;
            if (result == 11)
                return "0";
            if (result == 10)
                return "K";            
            return String.valueOf(result);            
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param rutClienteMas
     * @return
     */
    public static String printRutMas(String rutClienteMas) {
        if ( rutClienteMas == null || "".equalsIgnoreCase(rutClienteMas) ) {
            return "";
        }
        return formatoNumero(Double.parseDouble(rutClienteMas)) + "-" + calculaDigitoRut(rutClienteMas);
    }

}
