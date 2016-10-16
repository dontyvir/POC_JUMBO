package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DireccionesDAOException extends Exception {

	public DireccionesDAOException() {
		super("Exception, DireccionesDAOException Detectada!!!...");
	}

	public DireccionesDAOException(Throwable throwable) {
		super(throwable);
	}

	public DireccionesDAOException(String message) {
		super(message);
	}

	public DireccionesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DireccionesDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
