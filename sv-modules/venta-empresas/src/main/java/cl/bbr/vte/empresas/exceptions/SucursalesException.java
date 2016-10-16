package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class SucursalesException extends Exception {

	public SucursalesException() {
		super("Exception, SucursalesException Detectada!!!...");
	}

	public SucursalesException(Throwable throwable) {
		super(throwable);
	}

	public SucursalesException(String message) {
		super(message);
	}

	public SucursalesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SucursalesException(Exception e) {
		super(e.getMessage());
	}
	
	
}
