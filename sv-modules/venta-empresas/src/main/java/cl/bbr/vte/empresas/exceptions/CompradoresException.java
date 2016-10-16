package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CompradoresException extends Exception {

	public CompradoresException() {
		super("Exception, CompradoresException Detectada!!!...");
	}

	public CompradoresException(Throwable throwable) {
		super(throwable);
	}

	public CompradoresException(String message) {
		super(message);
	}

	public CompradoresException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CompradoresException(Exception e) {
		super(e.getMessage());
	}
	
	
}
