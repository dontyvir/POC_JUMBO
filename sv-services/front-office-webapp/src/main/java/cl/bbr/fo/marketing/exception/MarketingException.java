package cl.bbr.fo.marketing.exception;

/**
 * Excepci�n que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MarketingException extends java.lang.Exception {

	/**
	 * Constructor
	 */
	public MarketingException() {
		super("Exception, ClientesException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public MarketingException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de excepci�n
	 */
	public MarketingException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 * @param throwable	
	 */
	public MarketingException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepci�n
	 */
	public MarketingException(Exception e) {
		super(e.getMessage());
	}

}
