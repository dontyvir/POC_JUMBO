package cl.bbr.boc.utils;

import java.text.DecimalFormat;

public class Formatos {
	public static String formatoMoneda (String moneda){
		DecimalFormat formateador = new DecimalFormat("###,###.##");
		String monedaFormateada = "$"+formateador.format(Integer.parseInt(moneda));
		return monedaFormateada; 

	}

}
