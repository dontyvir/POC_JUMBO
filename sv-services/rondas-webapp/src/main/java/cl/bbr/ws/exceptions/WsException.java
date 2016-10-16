package cl.bbr.ws.exceptions;

/**
 * Excepciones del webservice
 */

public class WsException extends Exception {

    /**
     * The default constructor.
     */
	public WsException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of BolException
     *
     * @param throwable the parent Throwable
     */
    public WsException(Throwable throwable)
    {
		super(throwable);
    }
    
    /**
     * Constructs a new instance of BolException
     *
     * @param param the throwable message.
     */    
	public WsException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of BolException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public WsException(String message, Throwable throwable)
    {
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of BolException
	 *
	 * @param e exception
	 */
	public WsException(Exception e) {
		super(e.getMessage());
	} 	
	
}
