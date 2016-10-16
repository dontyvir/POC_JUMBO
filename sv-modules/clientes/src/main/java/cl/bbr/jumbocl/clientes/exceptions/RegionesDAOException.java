package cl.bbr.jumbocl.clientes.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegionesDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */	
	public RegionesDAOException() {
		super("Exception, RegionesDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public RegionesDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de excepción
	 */
	public RegionesDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public RegionesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public RegionesDAOException(Exception e) {
		super(e);
	}

}
