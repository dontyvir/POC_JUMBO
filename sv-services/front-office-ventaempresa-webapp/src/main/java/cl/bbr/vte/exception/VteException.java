package cl.bbr.vte.exception;

public class VteException extends Exception {

    /**
     * The default constructor.
     */
	public VteException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of VteException
     *
     * @param throwable the parent Throwable
     */
    public VteException(Throwable throwable)
    {
		super(throwable);
    }
    
    /**
     * Constructs a new instance of VteException
     *
     * @param param String.
     */    
	public VteException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of VteException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public VteException(String message, Throwable throwable)
    {
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of VteException
	 *
	 * @param e exception.
	 */
	public VteException(Exception e) {
		super(e.getMessage());
	} 	
	
}
