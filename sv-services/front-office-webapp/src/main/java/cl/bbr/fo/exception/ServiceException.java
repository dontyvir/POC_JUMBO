package cl.bbr.fo.exception;

/**
 * Excepción que es lanzada en caso de error en la capa de Servicios. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ServiceException extends Exception {

	/**
	 * Constructor
	 * 
	 */	
	public ServiceException() {
		super("Exception, ServiceException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 * @param arg1	Throwable	
	 */	
	public ServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepción
	 */	
	public ServiceException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */	
	public ServiceException(Throwable arg0) {
		super(arg0);
	}

	public ServiceException(Exception e) {
		super(e);
	}
	
	
}
