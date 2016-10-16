package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EmpresasDAOException extends Exception {

	public EmpresasDAOException() {
		super("Exception, EmpresasDAOException Detectada!!!...");
	}

	public EmpresasDAOException(Throwable throwable) {
		super(throwable);
	}

	public EmpresasDAOException(String message) {
		super(message);
	}

	public EmpresasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EmpresasDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
