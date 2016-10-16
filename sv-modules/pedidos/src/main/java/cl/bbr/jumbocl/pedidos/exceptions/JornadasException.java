package cl.bbr.jumbocl.pedidos.exceptions;

public class JornadasException extends Exception {

	public JornadasException() {
		super("Exception, PedidosException Detectada!!!...");
	}

	public JornadasException(Throwable throwable) {
		super(throwable);
	}

	public JornadasException(String message) {
		super(message);
	}

	public JornadasException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public JornadasException(Exception e) {
		super(e.getMessage());
	}	
	
	
}
