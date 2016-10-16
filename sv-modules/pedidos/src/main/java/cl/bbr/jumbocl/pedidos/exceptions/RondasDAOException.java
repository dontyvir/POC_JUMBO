package cl.bbr.jumbocl.pedidos.exceptions;

public class RondasDAOException extends Exception {
	
	public RondasDAOException() {
		super("Exception, RondasDAOExceptions Detectada!!!...");
	}

	public RondasDAOException(Throwable throwable) {
		super(throwable);
	}

	public RondasDAOException(String message) {
		super(message);
	}

	public RondasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RondasDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
