package cl.bbr.vte.cotizaciones.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CotizacionesException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */
	public CotizacionesException() {
		super("Exception, CotizacionesException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public CotizacionesException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public CotizacionesException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public CotizacionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public CotizacionesException(Exception e) {
		super(e.getMessage());
	}

}
