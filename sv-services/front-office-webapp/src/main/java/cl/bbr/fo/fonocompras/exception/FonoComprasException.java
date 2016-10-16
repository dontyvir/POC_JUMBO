package cl.bbr.fo.fonocompras.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FonoComprasException extends java.lang.Exception {
	
	public FonoComprasException() {
		super("Exception, FonoComprasException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public FonoComprasException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public FonoComprasException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public FonoComprasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public FonoComprasException(Exception e) {
		super(e.getMessage());
	}

}
