package cl.bbr.fo.faq.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FaqDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */	
	public FaqDAOException() {
		super("Exception, FaqDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public FaqDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public FaqDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public FaqDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public FaqDAOException(Exception e) {
		super(e.getMessage());
	}

}
