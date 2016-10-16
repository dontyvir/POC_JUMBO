package cl.bbr.jumbocl.shared.exceptions;

/**
 * Clase principal de manejo de excepciones.
 * 
 * @author BBR
 *
 */
public class DAOException extends Exception {

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
