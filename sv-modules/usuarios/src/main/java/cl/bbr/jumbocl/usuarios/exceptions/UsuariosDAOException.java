package cl.bbr.jumbocl.usuarios.exceptions;

public class UsuariosDAOException extends java.lang.Exception {
	
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
