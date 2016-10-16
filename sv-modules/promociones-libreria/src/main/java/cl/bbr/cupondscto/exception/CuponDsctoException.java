package cl.bbr.cupondscto.exception;

public class CuponDsctoException extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public CuponDsctoException() {
		super("Exception, CuponDsctoException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param throwable	
	 */
	public CuponDsctoException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 */
	public CuponDsctoException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message	Mensaje de exepción
	 * @param throwable	
	 */
	public CuponDsctoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Constructor
	 * 
	 * @param e	Excepción
	 */
	public CuponDsctoException(Exception e) {
		super(e.getMessage());
	}
}
