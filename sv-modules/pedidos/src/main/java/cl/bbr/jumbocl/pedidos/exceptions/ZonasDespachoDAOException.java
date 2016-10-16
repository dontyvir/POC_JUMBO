package cl.bbr.jumbocl.pedidos.exceptions;

public class ZonasDespachoDAOException extends Exception {

	public ZonasDespachoDAOException() {
		super("Exception, ZonasDespachoDAOException Detectada!!!...");
	}

	public ZonasDespachoDAOException(Throwable throwable) {
		super(throwable);
	}

	public ZonasDespachoDAOException(String message) {
		super(message);
	}

	public ZonasDespachoDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ZonasDespachoDAOException(Exception e) {
		super(e);
	}		
	
}
