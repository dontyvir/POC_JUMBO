/*
 * Creado el 04-04-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
package cl.bbr.jumbocl.common.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Clase con utilidades para el manejo de n�meros
 * @author jvillalobos
 *
 */
public class NumericUtils {

	public NumericUtils() {
	}
	
	/**
	 * Revisa si un n�mero de tipo 'double' tiene o no decimales significativos, de acuerdo al nivel de precisi�n que
	 * se requiera. Por ejemplo, si se quiere establecer si un n�mero tiene decimales significativos despu�s
	 * de la posici�n 2, precision = 2. As�, para el n�mero 123.01 se considera tiene decimales significativos,
	 * mientras que 123.001 no los tiene.
	 * @param number N�mero a verificar
	 * @param precision Cantida de decimales o precisi�n a considerar
	 * @return
	 */
	public static boolean tieneDecimalesSignificativos(double number, int precision) {
		String pattern = "#.";
		for (int i = 0; i < precision; i++)
			pattern += "#";
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat(pattern, dfs);
		double entero = Double.parseDouble(df.format(Math.floor(number)));
		double decimal = Double.parseDouble(df.format(number - entero));;
		double temp = decimal * Math.pow(10, precision);
		if (temp != 0)
			return true;
		return false;
	}

	public static void main(String[] args) {
	}
}
