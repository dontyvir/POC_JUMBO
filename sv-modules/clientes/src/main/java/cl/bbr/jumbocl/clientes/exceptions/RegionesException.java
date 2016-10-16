package cl.bbr.jumbocl.clientes.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegionesException extends java.lang.Exception {
	
	/**
	 * Constructor
	 */
	public RegionesException() {
		super("Exception, RegionesException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public RegionesException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de excepción
	 */
	public RegionesException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public RegionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public RegionesException(Exception e) {
		super(e);
	}

}
