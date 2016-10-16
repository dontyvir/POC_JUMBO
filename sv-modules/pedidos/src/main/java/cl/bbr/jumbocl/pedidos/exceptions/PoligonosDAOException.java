package cl.bbr.jumbocl.pedidos.exceptions;

public class PoligonosDAOException extends Exception {
	public PoligonosDAOException() {
		super("Exception, SectorPickingDAOException Detectada!!!...");
	}

	public PoligonosDAOException(Throwable throwable) {
		super(throwable);
	}

	public PoligonosDAOException(String message) {
		super(message);
	}

	public PoligonosDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PoligonosDAOException(Exception e) {
		super(e.getMessage());
	}
}
