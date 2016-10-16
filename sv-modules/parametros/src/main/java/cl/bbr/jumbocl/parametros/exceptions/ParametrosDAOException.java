package cl.bbr.jumbocl.parametros.exceptions;

public class ParametrosDAOException extends Exception {

	public ParametrosDAOException() {
		super("Exception, ParametrosDAOException Detectada!!!...");
	}

	public ParametrosDAOException(Throwable throwable) {
		super(throwable);
	}

	public ParametrosDAOException(String message) {
		super(message);
	}

	public ParametrosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParametrosDAOException(Exception e) {
		super(e);
	}	
	
}
