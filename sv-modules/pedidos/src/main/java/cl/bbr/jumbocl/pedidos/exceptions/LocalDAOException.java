package cl.bbr.jumbocl.pedidos.exceptions;

public class LocalDAOException extends Exception {

	public LocalDAOException() {
		super("Exception, LocalDAOException Detectada!!!...");
	}

	public LocalDAOException(Throwable throwable) {
		super(throwable);
	}

	public LocalDAOException(String message) {
		super(message);
	}

	public LocalDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public LocalDAOException(Exception e) {
		super(e.getMessage());
	}	
		
	
}
