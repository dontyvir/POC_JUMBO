package cl.bbr.jumbocl.parametros.exceptions;

public class ParametrosException extends Exception {

	public ParametrosException() {
		super("Exception, ParametrosException Detectada!!!...");
	}

	public ParametrosException(Throwable throwable) {
		super(throwable);
	}

	public ParametrosException(String message) {
		super(message);
	}

	public ParametrosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParametrosException(Exception e) {
		super(e);
	}
	
	
}
