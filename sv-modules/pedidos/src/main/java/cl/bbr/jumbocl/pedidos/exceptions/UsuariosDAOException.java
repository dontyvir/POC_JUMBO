package cl.bbr.jumbocl.pedidos.exceptions;

public class UsuariosDAOException extends Exception {

	public UsuariosDAOException() {
		super("Exception, UsuariosDAOException Detectada!!!...");
	}

	public UsuariosDAOException(Throwable throwable) {
		super(throwable);
	}

	public UsuariosDAOException(String message) {
		super(message);
	}

	public UsuariosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UsuariosDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
