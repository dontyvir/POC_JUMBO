package cl.jumbo.terminar.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Formatos para el fo
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class Formatos {

    /**
     * Formato para Fecha y Hora
     */
    public static final String DATE_TIME = "dd-MM-yyyy HH:mm:ss";

    /**
     * Formato para Fecha
     */
    public static final String DATE = "dd-MM-yyyy";

    /**
     * Formato para la Fecha del calendario
     */
    public static final String DATE_CAL = "dd-MM";

    /**
     * Formato para Fecha BD
     */
    public static final String DATE_BD = "yyyy-MM-dd";
    
    /**
     * Formato para la Hora del calendario
     */
    public static final String HOUR_CAL = "HH:mm";

    /**
     * Formato para la hora
     */
    public static final String TIME = "HH:mm:ss";

    /**
     * Formato para Fecha y Hora
     */
    public static final String YYYYMMDD = "yyyy-MM-dd";

    /**
     * Formato para el nombre de los dias
     */
    public static final String EEEEEEEEE = "EEEEEEEEE";

    /**
     * J: Formato para cantidades con decimales
     */

    public static final DecimalFormat cantidad = new DecimalFormat("#####.###");
    

    /**
     * Constructor
     *  
     */
    public Formatos() {
    }
    
    /**
     * J
     * @param numero
     * @return
     */
    public static String cantidad(Number numero){
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("######.###", dfs);
        return df.format(numero);
    }

    /**
     * Formato definido para el precio
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static String formatoPrecio(double num) {

        String numero = Math.round(num) + "";
        String numero_out = "";
        int j = 1;
        for (int i = numero.length(); i > 0; i--) {
            numero_out += numero.charAt(i - 1) + "";
            if (j != numero.length() && j % 3 == 0)
                numero_out += ".";
            j++;
        }
        numero = "$";
        for (int i = numero_out.length() - 1; i >= 0; i--)
            numero += numero_out.charAt(i);
        return numero;

    }

    /**
     * Formato definido para un valor de intervalo
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static String formatoIntervalo(double num) {

        double num_aux = Math.rint(num * 1000) / 1000;
        if (Math.floor(num_aux) == num_aux)
            return Math.round(Math.ceil(num_aux)) + "";
        return Math.rint(num_aux * 1000) / 1000 + "";

    }

    /**
     * Formato definido para la cantidad
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static long formatoCantidad(double num) {
        return Math.round(Math.ceil(num));
    }

    /**
     * Formato definido para unidad de medida
     * 
     * @param unidad
     *            Unidad a formatear
     * @return Texto formateado
     */
    public static String formatoUnidad(String unidad) {

        String texto = "";

        if ("KG".equals(unidad)) {
            texto = "Kg";
        } else
            texto = "Und";

        return texto;
    }

    /**
     * Elimina caracteres no válidos
     * 
     * @param valor
     *            Texto a revisar
     * @return Texto revisado
     */
    public static String stringToSearch(String valor) {

        String res = "";

        res = valor.replaceAll("[\\s]+", " ");
        res = res.replaceAll("[^A-Za-z0-9 áÁéÉíÍóÓúÚñÑüÜ]+", "");

        return res;

    }
    
    public static String sanitizeAccents(String word)
    {
    	String wSanitize = "";
    	if(word != null){
    		wSanitize = word.replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u');
    		wSanitize = wSanitize.replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
    	}
    	return wSanitize;
    }
    
    public static String sanitizeEne(String word)
    {
    	String wSanitize = "";
    	if(word != null){
    		wSanitize = word.replace('ñ', 'n');
    		wSanitize = wSanitize.replace('Ñ', 'N');
    	}
    	return wSanitize;
    }
    
    /**
     * mejorado
     * @param num
     * @param sw
     * @return
     */
    public static String formatoPrecio(double num, boolean sw) {

    	NumberFormat formatter = NumberFormat.getCurrencyInstance();
    	String moneyString = "";
    	try{ 
            moneyString = formatter.format(num);
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	
      return moneyString;
    }
    
    /**
     * 
     * @param cadena
     * @return
     */
    public static long unFormatoPrecio(String cadena) {

        //String numero = Math.round(num) + "";
        String numero_out = "";
        long numero = 0L;
        int j = 1;
        try{
	        char strArray[] = cadena.toCharArray();
	        for(int i=0; i < strArray.length; i++){
	        	 if( isNumero(strArray[i]))
	        		 numero_out += strArray[i];
	         }
	        numero = Long.parseLong(numero_out);
        }catch (Exception e) {
			// TODO: handle exception
		}
        return numero;
        }
       
        
    /**
     * 
     * @param c
     * @return
     */
    private static boolean isNumero(char c) {
		// TODO Auto-generated method stub
    	String[] digito = {"0","1","2","3","4","5","6","7","8","9"};
    	boolean value = false;
    	try{
    		String valor = c +" ";
    		for (int i = 0; i < digito.length; i++) {
				if( digito[i].equals(valor.trim())){
					value = true;
					break;
				}
			}
    	}catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

}
