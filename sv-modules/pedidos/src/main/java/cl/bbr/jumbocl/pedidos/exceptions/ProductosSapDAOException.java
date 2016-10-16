package cl.bbr.jumbocl.pedidos.exceptions;

public class ProductosSapDAOException extends Exception {

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
