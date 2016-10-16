package cl.bbr.jumbocl.pedidos.exceptions;

public class DespachosDAOException extends Exception {
	
	public DespachosDAOException() {
		super("Exception, DespachosDAOException Detectada!!!...");
	}

	public DespachosDAOException(Throwable throwable) {
		super(throwable);
	}

	public DespachosDAOException(String message) {
		super(message);
	}

	public DespachosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DespachosDAOException(Exception e) {
		super(e.getMessage());
	}	
}
