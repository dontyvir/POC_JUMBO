package cl.jumbo.ventaondemand.exceptions;

public class JsonException extends java.lang.Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonException() {
		super();
	}

	public JsonException(Throwable throwable) {
		super(throwable);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public JsonException(Exception e) {
		super(e);
	}
}
