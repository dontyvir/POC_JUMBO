package cl.bbr.jumbocl.pedidos.exceptions;

public class ComunasDAOException extends Exception {

	public ComunasDAOException() {
		super("Exception, CalendarioDAOException Detectada!!!...");
	}

	public ComunasDAOException(Throwable throwable) {
		super(throwable);
	}

	public ComunasDAOException(String message) {
		super(message);
	}

	public ComunasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ComunasDAOException(Exception e) {
		super(e.getMessage());
	}	
	
	
}
