package cl.bbr.jumbocl.pedidos.exceptions;

public class DespachosException extends Exception {

	public DespachosException() {
		super("Exception, DespachosExcepction Detectada!!!...");
	}

	public DespachosException(Throwable throwable) {
		super(throwable);
	}

	public DespachosException(String message) {
		super(message);
	}

	public DespachosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DespachosException(Exception e) {
		super(e.getMessage());
	}
	
}
