package cl.bbr.vte.cotizaciones.exception;

/**
 * Excepci�n que es lanzada en caso de error en la capa de DAO. 
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
	 * @param message	Mensaje de exepci�n
	 */
	public CotizacionesDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 * @param throwable	
	 */
	public CotizacionesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepci�n
	 */
	public CotizacionesDAOException(Exception e) {
		super(e.getMessage());
	}

}
