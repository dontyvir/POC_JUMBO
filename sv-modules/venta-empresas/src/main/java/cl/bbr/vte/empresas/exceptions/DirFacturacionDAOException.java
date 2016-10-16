package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DirFacturacionDAOException extends Exception {

	public DirFacturacionDAOException() {
		super("Exception, DirFacturacionDAOException Detectada!!!...");
	}

	public DirFacturacionDAOException(Throwable throwable) {
		super(throwable);
	}

	public DirFacturacionDAOException(String message) {
		super(message);
	}

	public DirFacturacionDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DirFacturacionDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
