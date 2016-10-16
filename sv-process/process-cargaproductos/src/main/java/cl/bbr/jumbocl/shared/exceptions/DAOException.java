package cl.bbr.jumbocl.shared.exceptions;

/**
 * Clase principal de manejo de excepciones.
 * 
 * @author BBR
 *
 */
public class DAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3809653745191257360L;

	public DAOException() {
		super("Exception, DAOException Detectada!!!...");
	}

	public DAOException(Throwable throwable) {
		super(throwable);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DAOException(Exception e) {
		super(e.getMessage());
	}
	
}
