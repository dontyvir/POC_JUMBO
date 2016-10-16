package cl.bbr.jumbocl.contenidos.exceptions;

public class EstadosDAOException extends java.lang.Exception {
	private final static long serialVersionUID = 1;
	
	public EstadosDAOException() {
		super("Exception, EstadosDAOException Detectada!!!...");
	}

	public EstadosDAOException(Throwable throwable) {
		super(throwable);
	}

	public EstadosDAOException(String message) {
		super(message);
	}

	public EstadosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public EstadosDAOException(Exception e) {
		super(e.getMessage());
	}
	
}
