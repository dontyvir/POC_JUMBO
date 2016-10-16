package cl.bbr.jumbocl.usuarios.exceptions;

public class PerfilesDAOException extends java.lang.Exception {

	public PerfilesDAOException() {
		super("Exception, PerfilesDAOException Detectada!!!...");
	}

	public PerfilesDAOException(Throwable throwable) {
		super(throwable);
	}

	public PerfilesDAOException(String message) {
		super(message);
	}

	public PerfilesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PerfilesDAOException(Exception e) {
		super(e.getMessage());
	}

}
