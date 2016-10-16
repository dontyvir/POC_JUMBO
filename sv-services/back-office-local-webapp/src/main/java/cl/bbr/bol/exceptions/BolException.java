package cl.bbr.bol.exceptions;

public class BolException extends Exception {

    /**
     * The default constructor.
     */
	public BolException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of BolException
     *
     * @param throwable the parent Throwable
     */
    public BolException(Throwable throwable)
    {
		super(throwable);
    }
    
    /**
     * Constructs a new instance of BolException
     *
     * @param param String.
     */    
	public BolException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of BolException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public BolException(String message, Throwable throwable)
    {
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of BolException
	 *
	 * @param e exception.
	 */
	public BolException(Exception e) {
		super(e.getMessage());
	} 	
	
}
