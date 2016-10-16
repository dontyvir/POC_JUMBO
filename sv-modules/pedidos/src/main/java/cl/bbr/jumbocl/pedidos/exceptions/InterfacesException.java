package cl.bbr.jumbocl.pedidos.exceptions;

public class InterfacesException extends Exception {

	public InterfacesException() {
		super("Exception, CalendarioException Detectada!!!...");
	}

	public InterfacesException(Throwable throwable) {
		super(throwable);
	}

	public InterfacesException(String message) {
		super(message);
	}

	public InterfacesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InterfacesException(Exception e) {
		super(e.getMessage());
	}		
	
	
}
