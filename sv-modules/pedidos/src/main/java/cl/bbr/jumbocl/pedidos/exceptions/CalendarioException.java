package cl.bbr.jumbocl.pedidos.exceptions;

public class CalendarioException extends Exception {

	public CalendarioException() {
		super("Exception, CalendarioException Detectada!!!...");
	}

	public CalendarioException(Throwable throwable) {
		super(throwable);
	}

	public CalendarioException(String message) {
		super(message);
	}

	public CalendarioException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CalendarioException(Exception e) {
		super(e.getMessage());
	}		
	
	
}
