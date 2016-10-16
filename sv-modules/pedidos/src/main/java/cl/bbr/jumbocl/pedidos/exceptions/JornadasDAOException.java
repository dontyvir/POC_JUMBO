package cl.bbr.jumbocl.pedidos.exceptions;

public class JornadasDAOException extends Exception {

	public JornadasDAOException() {
		super("Exception, JornadasDAOException Detectada!!!...");
	}

	public JornadasDAOException(Throwable throwable) {
		super(throwable);
	}

	public JornadasDAOException(String message) {
		super(message);
	}

	public JornadasDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public JornadasDAOException(Exception e) {
		super(e.getMessage());
	}		
	
}
