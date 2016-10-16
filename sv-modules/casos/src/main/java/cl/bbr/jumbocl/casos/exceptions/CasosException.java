package cl.bbr.jumbocl.casos.exceptions;

public class CasosException extends Exception {

	public CasosException() {
		super("Exception, CasosException Detectada!!!...");
	}

	public CasosException(Throwable throwable) {
		super(throwable);
	}

	public CasosException(String message) {
		super(message);
	}

	public CasosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CasosException(Exception e) {
		super(e.getMessage());
	}
	
	
}
