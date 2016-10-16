package cl.bbr.jumbocl.productos.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Control. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductosException extends java.lang.Exception {
	
	/**
	 * Constructor
	 * 
	 */
	public ProductosException() {
		super("Exception, ClientesException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public ProductosException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public ProductosException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public ProductosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción	
	 */
	public ProductosException(Exception e) {
		super(e);
	}

}
