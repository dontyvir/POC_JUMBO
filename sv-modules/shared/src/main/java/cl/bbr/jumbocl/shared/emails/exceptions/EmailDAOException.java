package cl.bbr.jumbocl.shared.emails.exceptions;

/**
 * Excepci�n que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EmailDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */
	public EmailDAOException() {
		super("Exception, EmailDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public EmailDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de excepci�n
	 */
	public EmailDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepci�n
	 * @param throwable	
	 */
	public EmailDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepci�n
	 */
	public EmailDAOException(Exception e) {
		super(e.getMessage());
	}

}
