package cl.bbr.jumbocl.pedidos.exceptions;

public class LocalException extends Exception {

	public LocalException() {
		super("Exception, LocalException Detectada!!!...");
	}

	public LocalException(Throwable throwable) {
		super(throwable);
	}

	public LocalException(String message) {
		super(message);
	}

	public LocalException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public LocalException(Exception e) {
		super(e.getMessage());
	}	
	
}
