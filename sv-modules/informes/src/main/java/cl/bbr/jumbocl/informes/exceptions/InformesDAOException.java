package cl.bbr.jumbocl.informes.exceptions;

public class InformesDAOException extends Exception {

	public InformesDAOException() {
		super("Exception, InformesDAOException Detectada!!!...");
	}

	public InformesDAOException(Throwable throwable) {
		super(throwable);
	}

	public InformesDAOException(String message) {
		super(message);
	}

	public InformesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InformesDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
