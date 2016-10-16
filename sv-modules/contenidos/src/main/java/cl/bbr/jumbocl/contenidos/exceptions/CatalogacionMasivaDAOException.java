package cl.bbr.jumbocl.contenidos.exceptions;

public class CatalogacionMasivaDAOException extends java.lang.Exception {
	private final static long serialVersionUID = 1;
	
	public CatalogacionMasivaDAOException() {
		super("Exception, ProductosDAOException Detectada!!!...");
	}

	public CatalogacionMasivaDAOException(Throwable throwable) {
		super(throwable);
	}

	public CatalogacionMasivaDAOException(String message) {
		super(message);
	}

	public CatalogacionMasivaDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CatalogacionMasivaDAOException(Exception e) {
		super(e.getMessage());
	}
	
}
