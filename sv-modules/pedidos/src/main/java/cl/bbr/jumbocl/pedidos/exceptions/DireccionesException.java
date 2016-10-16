package cl.bbr.jumbocl.pedidos.exceptions;

public class DireccionesException extends java.lang.Exception {
	
	public DireccionesException() {
		super("Exception, DireccionesException Detectada!!!...");
	}

	public DireccionesException(Throwable throwable) {
		super(throwable);
	}

	public DireccionesException(String message) {
		super(message);
	}

	public DireccionesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DireccionesException(Exception e) {
		super(e.getMessage());
	}

}
