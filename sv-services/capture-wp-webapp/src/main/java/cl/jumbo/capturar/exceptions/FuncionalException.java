package cl.jumbo.capturar.exceptions;

/**
 * Excepción que es lanzada en caso de error en la capa de negocios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FuncionalException extends Exception {

	/**
	 * Constructor
	 * 
	 */	
	public FuncionalException() {
		super("Exception, FuncionalException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 * @param arg1	Throwable	
	 */	
	public FuncionalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 */	
	public FuncionalException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */	
	public FuncionalException(Throwable arg0) {
		super(arg0);
	}

	
	
}
