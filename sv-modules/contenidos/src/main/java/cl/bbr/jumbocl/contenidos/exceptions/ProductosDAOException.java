package cl.bbr.jumbocl.contenidos.exceptions;

public class ProductosDAOException extends java.lang.Exception {
	private final static long serialVersionUID = 1;
	
	public ProductosDAOException() {
		super("Exception, ProductosDAOException Detectada!!!...");
	}

	public ProductosDAOException(Throwable throwable) {
		super(throwable);
	}

	public ProductosDAOException(String message) {
		super(message);
	}

	public ProductosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ProductosDAOException(Exception e) {
		super(e.getMessage());
	}
	
}
