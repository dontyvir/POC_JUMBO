package cl.bbr.jumbocl.pedidos.exceptions;

public class PedidosDAOException extends Exception {

	public PedidosDAOException() {
		super("Exception, PedidosDAOException Detectada!!!...");
	}

	public PedidosDAOException(Throwable throwable) {
		super(throwable);
	}

	public PedidosDAOException(String message) {
		super(message);
	}

	public PedidosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PedidosDAOException(Exception e) {
		super(e);
	}	
	
}
