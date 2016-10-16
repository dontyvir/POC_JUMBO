package cl.bbr.jumbocl.common.exceptions;

public class SystemException  extends Exception{

    /**
     * The default constructor.
     */
	public SystemException() {
		super("Exception de Sistema!!!");
	}

    /**
     * Constructs a new instance of SystemException
     *
     * @param throwable the parent Throwable
     */
    public SystemException(Throwable throwable){
		super(throwable);
    }
    
    /**
     * Constructs a new instance of SystemException
     *
     * @param param String
     */
	public SystemException(String param) {
		super(param);
	}	
	
	/**
	 * Constructs a new instance of SystemException
	 *
	 * @param message the throwable message.
	 * @param throwable the parent of this Throwable.
	 */
    public SystemException(String message, Throwable throwable){
        super(message,throwable);
	}	

	/**
	 * Constructs a new instance of SystemException
	 *
	 * @param e exception.
	 */
	public SystemException(Exception e) {
		super(e);
	}
}
