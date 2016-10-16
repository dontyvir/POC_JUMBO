package cl.bbr.jumbocl.productos.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductosDAOException extends java.lang.Exception {
	
	/**
	 * Constructor
	 * 
	 */
	public ProductosDAOException() {
		super("Exception, ClientesDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public ProductosDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public ProductosDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public ProductosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción	
	 */
	public ProductosDAOException(Exception e) {
		super(e);
	}

}
