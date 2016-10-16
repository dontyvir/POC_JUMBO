package cl.bbr.jumbocl.contenidos.exceptions;

public class CategoriasException extends java.lang.Exception {
	
	public CategoriasException() {
		super("Exception, CategoriasException Detectada!!!...");
	}

	public CategoriasException(Throwable throwable) {
		super(throwable);
	}

	public CategoriasException(String message) {
		super(message);
	}

	public CategoriasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CategoriasException(Exception e) {
		super(e.getMessage());
	}

}
