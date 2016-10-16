package cl.bbr.irsmonitor.exceptions;

public class IrsMonException extends Exception  {

    /**
     * The default constructor.
     */
	public IrsMonException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of BolException
     *
     * @param throwable the parent Throwable
     */
    public IrsMonException(Throwable throwable)
    {
		super(throwable);
    }
    
    /**
     * Constructs a new instance of BolException
     *
     * @param message the throwable message.
     */    
	public IrsMonException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of BolException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public IrsMonException(String message, Throwable throwable)
    {
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of BolException
	 *
	 * @param exception.
	 */
	public IrsMonException(Exception e) {
		super(e.getMessage());
	} 	
}
