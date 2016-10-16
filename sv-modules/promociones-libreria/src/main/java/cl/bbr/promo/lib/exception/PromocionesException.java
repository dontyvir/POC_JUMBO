package cl.bbr.promo.lib.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Control. 
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
	 * @param message	Mensaje de exepción
	 */
	public PromocionesException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public PromocionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public PromocionesException(Exception e) {
		super(e);
	}

}
