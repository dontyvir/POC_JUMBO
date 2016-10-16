package cl.bbr.fo.fonocompras.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FonoComprasDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 * 
	 */	
	public FonoComprasDAOException() {
		super("Exception, FonoComprasDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public FonoComprasDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public FonoComprasDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public FonoComprasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e 	Excepción
	 */
	public FonoComprasDAOException(Exception e) {
		super(e.getMessage());
	}

}
