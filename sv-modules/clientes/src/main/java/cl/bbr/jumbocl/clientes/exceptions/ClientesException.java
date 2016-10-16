package cl.bbr.jumbocl.clientes.exceptions;

public class ClientesException extends java.lang.Exception {
	
	public ClientesException() {
		super("Exception, ClientesException Detectada!!!...");
	}

	public ClientesException(Throwable throwable) {
		super(throwable);
	}

	public ClientesException(String message) {
		super(message);
	}

	public ClientesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ClientesException(Exception e) {
		super(e);
	}

}
