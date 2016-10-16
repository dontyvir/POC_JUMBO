package cl.bbr.jumbocl.pedidos.exceptions;

public class CalendarioDAOException extends Exception {

	public CalendarioDAOException() {
		super("Exception, ComunasDAOException Detectada!!!...");
	}

	public CalendarioDAOException(Throwable throwable) {
		super(throwable);
	}

	public CalendarioDAOException(String message) {
		super(message);
	}

	public CalendarioDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CalendarioDAOException(Exception e) {
		super(e);
	}	
	
}
