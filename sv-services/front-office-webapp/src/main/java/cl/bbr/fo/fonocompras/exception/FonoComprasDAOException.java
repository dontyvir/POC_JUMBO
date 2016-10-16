package cl.bbr.fo.fonocompras.exception;

/**
 * Excepci�n que es lanzada en caso de error en la capa de DAO. 
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
	 * @param message	Mensaje de exepci�n
	 */
	public FonoComprasDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 * @param throwable	
	 */
	public FonoComprasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e 	Excepci�n
	 */
	public FonoComprasDAOException(Exception e) {
		super(e.getMessage());
	}

}
