package cl.bbr.jumbocl.pedidos.exceptions;

public class PedidosException extends Exception {

	public PedidosException() {
		super("Exception, PedidosException Detectada!!!...");
	}

	public PedidosException(Throwable throwable) {
		super(throwable);
	}

	public PedidosException(String message) {
		super(message);
	}

	public PedidosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PedidosException(Exception e) {
		super(e.getMessage());
	}
	
	
}
