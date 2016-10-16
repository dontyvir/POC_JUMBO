package cl.bbr.jumbocl.eventos.exceptions;

public class EventosException extends Exception {

	public EventosException() {
		super("Exception, CasosException Detectada!!!...");
	}

	public EventosException(Throwable throwable) {
		super(throwable);
	}

	public EventosException(String message) {
		super(message);
	}

	public EventosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EventosException(Exception e) {
		super(e.getMessage());
	}
	
	
}
