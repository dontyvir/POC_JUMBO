package cl.bbr.jumbocl.usuarios.exceptions;

public class UsuariosException extends Exception{
	public UsuariosException() {
		super("Exception, UsuariosException Detectada!!!...");
	}

	public UsuariosException(Throwable throwable) {
		super(throwable);
	}

	public UsuariosException(String message) {
		super(message);
	}

	public UsuariosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UsuariosException(Exception e) {
		super(e.getMessage());
	}

}
