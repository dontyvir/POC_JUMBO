package cl.bbr.vte.empresas.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DirFacturacionException extends Exception {

	public DirFacturacionException() {
		super("Exception, DirFacturacionException Detectada!!!...");
	}

	public DirFacturacionException(Throwable throwable) {
		super(throwable);
	}

	public DirFacturacionException(String message) {
		super(message);
	}

	public DirFacturacionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DirFacturacionException(Exception e) {
		super(e.getMessage());
	}
	
	
}
