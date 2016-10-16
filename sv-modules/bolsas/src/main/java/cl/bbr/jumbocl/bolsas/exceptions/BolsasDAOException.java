package cl.bbr.jumbocl.bolsas.exceptions;

public class BolsasDAOException extends Exception {

	public BolsasDAOException() {
		super("Exception, InformesDAOException Detectada!!!...");
	}

	public BolsasDAOException(Throwable throwable) {
		super(throwable);
	}

	public BolsasDAOException(String message) {
		super(message);
	}

	public BolsasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BolsasDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
