package cl.bbr.jumbocl.pedidos.exceptions;

public class SectorPickingDAOException extends Exception {
	public SectorPickingDAOException() {
		super("Exception, SectorPickingDAOException Detectada!!!...");
	}

	public SectorPickingDAOException(Throwable throwable) {
		super(throwable);
	}

	public SectorPickingDAOException(String message) {
		super(message);
	}

	public SectorPickingDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SectorPickingDAOException(Exception e) {
		super(e.getMessage());
	}
}
