package cl.bbr.fo.exception;

/**
 * Excepción que es lanzada en caso de necesitar tomar decisiones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class WarningException extends Exception {

	/**
	 * Constructor
	 */	
	public WarningException() {
		super("Exception, WarningException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 * @param arg1	Throwable	
	 */	
	public WarningException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 */	
	public WarningException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */	
	public WarningException(Throwable arg0) {
		super(arg0);
	}

	
	
}
