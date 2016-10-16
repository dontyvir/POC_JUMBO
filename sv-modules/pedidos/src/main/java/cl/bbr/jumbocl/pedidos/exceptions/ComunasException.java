package cl.bbr.jumbocl.pedidos.exceptions;

public class ComunasException extends Exception {

	public ComunasException() {
		super("Exception, ComunasException Detectada!!!...");
	}

	public ComunasException(Throwable throwable) {
		super(throwable);
	}

	public ComunasException(String message) {
		super(message);
	}

	public ComunasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ComunasException(Exception e) {
		super(e.getMessage());
	}		
	
	
	
}
