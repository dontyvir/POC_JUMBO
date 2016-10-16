package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DireccionesException extends Exception {

	public DireccionesException() {
		super("Exception, CompradoresException Detectada!!!...");
	}

	public DireccionesException(Throwable throwable) {
		super(throwable);
	}

	public DireccionesException(String message) {
		super(message);
	}

	public DireccionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DireccionesException(Exception e) {
		super(e.getMessage());
	}
	
	
}
