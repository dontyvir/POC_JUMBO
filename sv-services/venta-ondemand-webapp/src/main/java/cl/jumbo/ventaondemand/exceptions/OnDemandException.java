package cl.jumbo.ventaondemand.exceptions;

public class OnDemandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OnDemandException() {
		super();
	}

	public OnDemandException(Throwable throwable) {
		super(throwable);
	}

	public OnDemandException(String message) {
		super(message);
	}

	public OnDemandException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public OnDemandException(Exception e) {
		super(e);
	}
}
