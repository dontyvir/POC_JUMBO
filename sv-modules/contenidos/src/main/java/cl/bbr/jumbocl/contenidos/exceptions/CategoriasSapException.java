package cl.bbr.jumbocl.contenidos.exceptions;

public class CategoriasSapException extends Exception {
	private final static long serialVersionUID = 1;

	public CategoriasSapException() {
		super("Exception, CategoriasSapException Detectada!!!...");
	}

	public CategoriasSapException(Throwable throwable) {
		super(throwable);
	}

	public CategoriasSapException(String message) {
		super(message);
	}

	public CategoriasSapException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CategoriasSapException(Exception e) {
		super(e.getMessage());
	}}
