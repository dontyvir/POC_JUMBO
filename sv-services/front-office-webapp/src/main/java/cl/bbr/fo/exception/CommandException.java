package cl.bbr.fo.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Command. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CommandException extends Exception {

	/**
	 * Constructor
	 */
	public CommandException() {
		super("Exception, CommandException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 * @param arg1	Throwable	
	 */	
	public CommandException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 */	
	public CommandException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */	
	public CommandException(Throwable arg0) {
		super(arg0);
	}

	
	
}
