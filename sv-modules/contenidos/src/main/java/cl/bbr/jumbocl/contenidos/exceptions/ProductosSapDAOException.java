package cl.bbr.jumbocl.contenidos.exceptions;

public class ProductosSapDAOException extends Exception {
	private final static long serialVersionUID = 1;
	
	public ProductosSapDAOException() {
		super("Exception, ProductosSapDAOException Detectada!!!...");
	}

	public ProductosSapDAOException(Throwable throwable) {
		super(throwable);
	}

	public ProductosSapDAOException(String message) {
		super(message);
	}

	public ProductosSapDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ProductosSapDAOException(Exception e) {
		super(e.getMessage());
	}
	
}
