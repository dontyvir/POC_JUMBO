package cl.bbr.fo.util;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.log_app.Logging;

/**
 * Clase que permite agregar tracking del web
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class Tracking_web {

	/**
	 * Instancia del log
	 */
	private static Logging logger = new Logging(Tracking_web.class);	
	
	/**
	 * Constructor
	 */
	public Tracking_web() {
	}
	
	/**
	 * Graba tracking
	 * 
	 * @param seccion	Sección
	 * @param arg0		Datos del navegador y URL
	 * @return			True: éxito, False: error
	 */
	public static boolean saveTracking(String seccion, HashMap arg0) {
		
		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();		
		
		Long rut = new Long("0");

		//HttpSession session = "";//arg0.getSession();
		String rut_in ="";// (String)session.getAttribute("ses_cli_rut");
		if( rut_in != null && rut_in.compareTo("") != 0 ) {
			rut = new Long( rut_in );
		}

		try {
			biz.addTraking( seccion, rut, arg0 );
		} catch (SystemException e) {
			
			logger.debug("Error en traking web");
			
		}
		
		return true;

	}

}
