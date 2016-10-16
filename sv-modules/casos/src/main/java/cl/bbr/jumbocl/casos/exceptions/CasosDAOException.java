package cl.bbr.jumbocl.casos.exceptions;

public class CasosDAOException extends Exception {

	public CasosDAOException() {
		super("Exception, CasosDAOException Detectada!!!...");
	}

	public CasosDAOException(Throwable throwable) {
		super(throwable);
	}

	public CasosDAOException(String message) {
		super(message);
	}

	public CasosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CasosDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
