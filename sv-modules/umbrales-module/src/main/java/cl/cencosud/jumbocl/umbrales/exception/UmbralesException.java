package cl.cencosud.jumbocl.umbrales.exception;

public class UmbralesException extends Exception  {

    /**
     * The default constructor.
     */
	public UmbralesException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of BolException
     *
     * @param throwable the parent Throwable
     */
    public UmbralesException(Throwable throwable)
    {
		super(throwable);
    }
    
    /**
     * Constructs a new instance of BolException
     *
     * @param param String
     */    
	public UmbralesException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of BolException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public UmbralesException(String message, Throwable throwable)
    {
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of BolException
	 *
	 * @param e exception.
	 */
	public UmbralesException(Exception e) {
		super(e);
	} 	
}
