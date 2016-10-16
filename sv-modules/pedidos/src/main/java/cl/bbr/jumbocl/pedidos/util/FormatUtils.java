package cl.bbr.jumbocl.pedidos.util;

public class FormatUtils {

	//
	// Constantes de alineacion
	//
	/** Tipo de alineación a la izquierda */
	public static final int ALIGN_LEFT			= 0;
	/** Tipo de alineación a la derecha */
	public static final int ALIGN_RIGHT			= 1;
	
	
	
	/**
	 * formatea texto con largo máximo, rellena con espacios en blanco
	 * @param String cadena
	 * @param int largo
	 * @param int alineamiento
	 * @param char caracter de relleno
	 * @return
	 */
	public static String formatField(String cadena, int largocampo, int alineamiento, String caracter){
		
		int largocadena;
		String out = "";
		
		if ( cadena == null){
			return addCharToString(out,caracter,largocampo,alineamiento);
		}
		
		largocadena = cadena.length();
		
		if ( largocadena > largocampo )
			out = cadena.substring(0,largocampo);
		else
			out = addCharToString(cadena,caracter,largocampo,alineamiento);
	
		return out;
		
	}

	/**
	 * Agrega caracteres a un string
	 * @param str String original
	 * @param chr String(caracter) que se agregara a string original
	 * @param len Largo final de string modificado
	 * @param pos Posicion donde se alineará el texto original (ALIGN_LEFT, ALIGN_RIGHT)
	 * @return - String modificado
	 */
	public static String addCharToString(String str, String chr, int len, int pos) {
		String tmp = new String();
		len = len - str.length();

		for ( int cont = 0; cont < len; cont++ )
			tmp = tmp + chr.charAt(0);

		if (pos == Utiles.ALIGN_RIGHT)
			str = tmp + str;

		if (pos == Utiles.ALIGN_LEFT)
			str = str + tmp;

		return str;
	}
		
}
