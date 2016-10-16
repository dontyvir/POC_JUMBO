package cl.bbr.cupondscto.exception;

public class CuponDsctoDAOException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */	
	public CuponDsctoDAOException() {
		super("Exception, CuponDsctoDAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public CuponDsctoDAOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public CuponDsctoDAOException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public CuponDsctoDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public CuponDsctoDAOException(Exception e) {
		super(e);
	}

}
