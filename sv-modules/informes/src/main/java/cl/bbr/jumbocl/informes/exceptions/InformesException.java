package cl.bbr.jumbocl.informes.exceptions;

public class InformesException extends Exception {

	public InformesException() {
		super("Exception, InformesException Detectada!!!...");
	}

	public InformesException(Throwable throwable) {
		super(throwable);
	}

	public InformesException(String message) {
		super(message);
	}

	public InformesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InformesException(Exception e) {
		super(e.getMessage());
	}
	
	
}
