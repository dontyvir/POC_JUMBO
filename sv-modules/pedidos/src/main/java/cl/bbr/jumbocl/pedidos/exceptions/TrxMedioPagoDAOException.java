package cl.bbr.jumbocl.pedidos.exceptions;

public class TrxMedioPagoDAOException extends Exception {

	public TrxMedioPagoDAOException() {
		super("Exception, PedidosDAOException Detectada!!!...");
	}

	public TrxMedioPagoDAOException(Throwable throwable) {
		super(throwable);
	}

	public TrxMedioPagoDAOException(String message) {
		super(message);
	}

	public TrxMedioPagoDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TrxMedioPagoDAOException(Exception e) {
		super(e.getMessage());
	}	
	
}
