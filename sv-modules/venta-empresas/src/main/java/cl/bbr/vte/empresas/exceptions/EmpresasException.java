package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class EmpresasException extends Exception {
	

	public EmpresasException() {
		super("Exception, EmpresasException Detectada!!!...");
	}

	public EmpresasException(Throwable throwable) {
		super(throwable);
	}

	public EmpresasException(String message) {
		super(message);
	}

	public EmpresasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EmpresasException(Exception e) {
		super(e.getMessage());
	}
	
	
}
