package cl.bbr.jumbocl.eventos.exceptions;

public class EventosDAOException extends Exception {

	public EventosDAOException() {
		super("Exception, CasosDAOException Detectada!!!...");
	}

	public EventosDAOException(Throwable throwable) {
		super(throwable);
	}

	public EventosDAOException(String message) {
		super(message);
	}

	public EventosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EventosDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
