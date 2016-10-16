package cl.bbr.promo.lib.exception;

/**
 * Excepci�n que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class PromocionesException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */
	public PromocionesException() {
		super("Exception, ClientesException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public PromocionesException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 */
	public PromocionesException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 * @param throwable	
	 */
	public PromocionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepci�n
	 */
	public PromocionesException(Exception e) {
		super(e);
	}

}
