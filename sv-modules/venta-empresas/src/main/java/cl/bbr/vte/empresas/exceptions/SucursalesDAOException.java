package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class SucursalesDAOException extends Exception {

	public SucursalesDAOException() {
		super("Exception, SucursalesDAOException Detectada!!!...");
	}

	public SucursalesDAOException(Throwable throwable) {
		super(throwable);
	}

	public SucursalesDAOException(String message) {
		super(message);
	}

	public SucursalesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SucursalesDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
