package cl.bbr.vte.utils;

/**
 * Formatos para el fo
 * 
 * @author BBR e-commerce & retail
 *
 */
public class vteFormatos {

	/**
	 * Formato para Fecha y Hora
	 */
	public static final String DATE_TIME 	= "dd-MM-yyyy HH:mm:ss";
	/**
	 * Formato para Fecha
	 */
	public static final String DATE 		= "dd-MM-yyyy";
	/**
	 * Formato para la Fecha del calendario
	 */
	public static final String DATE_CAL 	= "dd-MM";
	/**
	 * Formato para la hora
	 */
	public static final String TIME 		= "HH:mm:ss";

	/**
	 * Constructor
	 *
	 */
	public vteFormatos() {
	}

	/**
	 * Formato definido para el precio 
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static String formatoPrecio( double num ) {

		String numero = Math.round(num )+"";
		String numero_out = "";
		int j = 1;
		for( int i = numero.length(); i > 0; i-- ) {
			numero_out += numero.charAt(i-1)+"";
			if( j!=numero.length() && j%3 == 0 )
				numero_out += ".";
			j++;
		}
		numero = "$";
		for(int i = numero_out.length()-1; i >= 0 ; i-- )
			numero += numero_out.charAt(i);
		return numero;

	}
	
	/**
	 * Formato definido para un valor de intervalo
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static String formatoIntervalo( double num ) {
		
		double num_aux = Math.rint(num*1000)/1000;
		if( Math.floor(num_aux) == num_aux )
			return Math.round(Math.ceil( num_aux ))+"";
		else
			return Math.rint(num_aux*1000)/1000+"";

	}	

	/**
	 * Formato definido para un valor de intervalo
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static String formatoIntervalo1( double num ) {
		
		double num_aux = Math.rint(num*10)/10;
		if( Math.floor(num_aux) == num_aux )
			return Math.round(Math.ceil( num_aux ))+"";
		else
			return Math.rint(num_aux*1000)/1000+"";

	}	
	
	/**
	 * Formato definido para la cantidad
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static long formatoCantidad( double num ) {
		return Math.round(Math.ceil( num ));
	}
	
	/**
	 * Formato definido para el precio 
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static String formatoCantidad2( double num ) {

		String numero = Math.round(Math.ceil(num ))+"";
		String numero_out = "";
		int j = 1;
		for( int i = numero.length(); i > 0; i-- ) {
			numero_out += numero.charAt(i-1)+"";
			if( j!=numero.length() && j%3 == 0 )
				numero_out += ".";
			j++;
		}
		numero = "";
		for(int i = numero_out.length()-1; i >= 0 ; i-- )
			numero += numero_out.charAt(i);
		return numero;

	}
	
	
	/**
	 * Formato definido para unidad de medida
	 * 
	 * @param unidad Unidad a formatear
	 * @return Texto formateado
	 */
	public static String formatoUnidad( String unidad ) {

		String texto = "";
		
		if( unidad.compareTo("KG") == 0 ) {
			texto = "Kg";
		}
		else
			texto = "Uni";
		
		return texto;
	}
		
	/**
	 * Elimina caracteres no válidos
	 * 
	 * @param valor Texto a revisar
	 * @return Texto revisado
	 */	
	public static String stringToSearch( String valor ) {
		
		String res = "";
		
		res = valor.replaceAll("[\\s]+", " ");
		res = res.replaceAll("[^A-Za-z0-9 áÁéÉíÍóÓúÚñÑüÜ]+", "");
				
		return res;
		
	}	
	
}
