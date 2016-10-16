package cl.bbr.fo.exception;

/**
 * Excepción que es lanzada en caso de error de sistema. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class SystemException extends Exception {

	/**
	 * Constructor
	 */	
	public SystemException() {
		super("Exception, SystemException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 * @param arg1	Throwable	
	 */	
	public SystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 */	
	public SystemException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */	
	public SystemException(Throwable arg0) {
		super(arg0);
	}

	
	
}
