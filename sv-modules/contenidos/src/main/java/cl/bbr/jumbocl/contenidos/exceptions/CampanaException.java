package cl.bbr.jumbocl.contenidos.exceptions;

public class CampanaException extends Exception {
	private final static long serialVersionUID = 1;

	public CampanaException() {
		super("Exception, CampanaException Detectada!!!...");
	}

	public CampanaException(Throwable throwable) {
		super(throwable);
	}

	public CampanaException(String message) {
		super(message);
	}

	public CampanaException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CampanaException(Exception e) {
		super(e.getMessage());
	}}
