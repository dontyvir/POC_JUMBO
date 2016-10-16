package cl.bbr.vte.cotizaciones.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CotizacionesDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */	
	public CotizacionesDAOException() {
		super("Exception, CotizacionesDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public CotizacionesDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public CotizacionesDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public CotizacionesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public CotizacionesDAOException(Exception e) {
		super(e.getMessage());
	}

}
