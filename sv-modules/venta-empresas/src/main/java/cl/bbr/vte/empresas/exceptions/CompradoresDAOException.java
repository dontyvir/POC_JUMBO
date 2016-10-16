package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CompradoresDAOException extends Exception {

	public CompradoresDAOException() {
		super("Exception, CompradoresDAOException Detectada!!!...");
	}

	public CompradoresDAOException(Throwable throwable) {
		super(throwable);
	}

	public CompradoresDAOException(String message) {
		super(message);
	}

	public CompradoresDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CompradoresDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
