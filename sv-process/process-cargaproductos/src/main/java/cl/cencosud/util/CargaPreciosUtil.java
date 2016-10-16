package cl.cencosud.util;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import cl.cencosud.constantes.Constantes;

public class CargaPreciosUtil {

	static {
		LogUtil.initLog4J();
	}
	
	private static Logger logger = Logger.getLogger(CargaPreciosUtil.class);	
	

	/**
	 * Carga un archivo .json y retorna un HashMap con la unidades de medida.
	 * 
	 * @return HashMap
	 */	
	public static HashMap getUnitsFromJson(){					 
		HashMap unitMap = null;		
		try {		
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
    		URL url = loader.getResource(Constantes.FILE_UNIDADES_JSON);  
    					
			JSONObject unitsJsonObj = JSONObject.fromObject( IOUtils.toString(url.openStream()) ).getJSONObject("unidades");				
			unitMap = iteraUnitsMedida(unitsJsonObj);			

		} catch (Exception e) {
			  logger.error("Error al cargar unidades de medida de " +Constantes.FILE_UNIDADES_JSON +" " + e);
		}				
		return unitMap;
	}
	
	/**
	 * Retorna un HashMap con la unidades de medida.
	 *   	 	
	 * @param unitsJsonObj, objeto que contiene las unidades de medida.
	 * 
	 * @return HashMap
	 */	
	private static HashMap iteraUnitsMedida(JSONObject unitsJsonObj) throws Exception{
		HashMap unitsMap = new HashMap();
		Iterator entries = unitsJsonObj.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  unitsMap.put((String) thisEntry.getKey(), (String) thisEntry.getValue());
		}
		return unitsMap;
	}
	
	
	/**
	 * Metodo convertMonedaPeso 
	 * e.g 12345678 to $ 12.345.678 or -12345 to $ -12.345
	 * 
	 * @param value moneda a formatear.
	 * @param pattern  patron.
	 * 
	 * @return String, moneda formateado.
	 */

	public static String convertMonedaPeso(String value, String pattern) {

		NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("es", "CL"));
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;

		decimalFormat.applyPattern(pattern);
		decimalFormat.setMaximumFractionDigits(0);
		String retorno = (value != null) ? value.trim() : "";

		if (!"".equals(retorno)) {
			try{
				retorno = decimalFormat.format(Double.parseDouble(retorno));
			}
			catch (Throwable t) {
				logger.error("Error al convertir ["+value+"] con patron ["+pattern+"]");
				retorno = value;
			}
		}
		return retorno;
	}

}
