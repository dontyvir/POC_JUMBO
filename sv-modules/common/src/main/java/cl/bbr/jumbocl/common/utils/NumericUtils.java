/*
 * Creado el 04-04-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.common.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Clase con utilidades para el manejo de números
 * @author jvillalobos
 *
 */
public class NumericUtils {

	public NumericUtils() {
	}
	
	/**
	 * Revisa si un número de tipo 'double' tiene o no decimales significativos, de acuerdo al nivel de precisión que
	 * se requiera. Por ejemplo, si se quiere establecer si un número tiene decimales significativos después
	 * de la posición 2, precision = 2. Así, para el número 123.01 se considera tiene decimales significativos,
	 * mientras que 123.001 no los tiene.
	 * @param number Número a verificar
	 * @param precision Cantida de decimales o precisión a considerar
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
