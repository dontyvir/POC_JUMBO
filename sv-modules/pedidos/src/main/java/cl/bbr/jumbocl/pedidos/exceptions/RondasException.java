package cl.bbr.jumbocl.pedidos.exceptions;

public class RondasException extends Exception {

	public RondasException() {
		super("Exception, RondasException Detectada!!!...");
	}

	public RondasException(Throwable throwable) {
		super(throwable);
	}

	public RondasException(String message) {
		super(message);
	}

	public RondasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RondasException(Exception e) {
		super(e.getMessage());
	}	
	
	
}
