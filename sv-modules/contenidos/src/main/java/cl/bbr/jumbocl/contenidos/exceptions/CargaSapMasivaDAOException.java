package cl.bbr.jumbocl.contenidos.exceptions;

public class CargaSapMasivaDAOException extends java.lang.Exception {
	private final static long serialVersionUID = 1;
	
	public CargaSapMasivaDAOException() {
		super("Exception, ProductosDAOException Detectada!!!...");
	}

	public CargaSapMasivaDAOException(Throwable throwable) {
		super(throwable);
	}

	public CargaSapMasivaDAOException(String message) {
		super(message);
	}

	public CargaSapMasivaDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CargaSapMasivaDAOException(Exception e) {
		super(e.getMessage());
	}
	
}
