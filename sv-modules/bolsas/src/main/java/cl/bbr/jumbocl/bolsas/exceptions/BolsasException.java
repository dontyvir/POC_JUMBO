package cl.bbr.jumbocl.bolsas.exceptions;

public class BolsasException extends Exception {

	public BolsasException() {
		super("Exception, InformesException Detectada!!!...");
	}

	public BolsasException(Throwable throwable) {
		super(throwable);
	}

	public BolsasException(String message) {
		super(message);
	}

	public BolsasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BolsasException(Exception e) {
		super(e.getMessage());
	}
	
	
}
