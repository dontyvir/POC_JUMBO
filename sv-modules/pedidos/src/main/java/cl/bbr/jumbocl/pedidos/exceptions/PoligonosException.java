package cl.bbr.jumbocl.pedidos.exceptions;

public class PoligonosException extends Exception {

	public PoligonosException() {
		super("Exception, PedidosException Detectada!!!...");
	}

	public PoligonosException(Throwable throwable) {
		super(throwable);
	}

	public PoligonosException(String message) {
		super(message);
	}

	public PoligonosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PoligonosException(Exception e) {
		super(e.getMessage());
	}
	
	
}
