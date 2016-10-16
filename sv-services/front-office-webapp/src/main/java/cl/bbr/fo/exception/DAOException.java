package cl.bbr.fo.exception;

/**
 * Excepci�n que es lanzada en caso de error en la capa de DAO. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class DAOException extends Exception {

	/**
	 * Constructor
	 */
	public DAOException() {
		super("Exception, DAOException Detectada!!!...");
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepci�n
	 * @param arg1	Throwable	
	 */	
	public DAOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Mensaje de exepci�n
	 */	
	public DAOException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0	Throwable	
	 */
	public DAOException(Throwable arg0) {
		super(arg0);
	}

	
	
}
