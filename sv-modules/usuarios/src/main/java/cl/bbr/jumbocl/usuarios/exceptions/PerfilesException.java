package cl.bbr.jumbocl.usuarios.exceptions;

public class PerfilesException extends Exception{
	public PerfilesException() {
		super("Exception, PerfilesException Detectada!!!...");
	}

	public PerfilesException(Throwable throwable) {
		super(throwable);
	}

	public PerfilesException(String message) {
		super(message);
	}

	public PerfilesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PerfilesException(Exception e) {
		super(e.getMessage());
	}

}
