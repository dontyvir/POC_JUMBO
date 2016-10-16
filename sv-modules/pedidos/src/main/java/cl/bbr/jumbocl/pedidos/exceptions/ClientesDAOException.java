package cl.bbr.jumbocl.pedidos.exceptions;

public class ClientesDAOException extends java.lang.Exception {
	
	public ClientesDAOException() {
		super("Exception, ClientesDAOException Detectada!!!...");
	}

	public ClientesDAOException(Throwable throwable) {
		super(throwable);
	}

	public ClientesDAOException(String message) {
		super(message);
	}

	public ClientesDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ClientesDAOException(Exception e) {
		super(e.getMessage());
	}


}
