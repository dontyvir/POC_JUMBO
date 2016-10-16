package cl.bbr.jumbocl.contenidos.exceptions;

public class CategoriasSapDAOException extends Exception {
	private final static long serialVersionUID = 1;
	
	public CategoriasSapDAOException() {
		super("Exception, CategoriasSapDAOException Detectada!!!...");
	}

	public CategoriasSapDAOException(Throwable throwable) {
		super(throwable);
	}

	public CategoriasSapDAOException(String message) {
		super(message);
	}

	public CategoriasSapDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CategoriasSapDAOException(Exception e) {
		super(e.getMessage());
	}


}
