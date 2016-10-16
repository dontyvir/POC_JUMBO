package cl.bbr.jumbocl.contenidos.exceptions;

public class ElementoException extends Exception {
	private final static long serialVersionUID = 1;

	public ElementoException() {
		super("Exception, ElementoException Detectada!!!...");
	}

	public ElementoException(Throwable throwable) {
		super(throwable);
	}

	public ElementoException(String message) {
		super(message);
	}

	public ElementoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ElementoException(Exception e) {
		super(e.getMessage());
	}}
