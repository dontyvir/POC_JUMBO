package cl.cencosud.jumbocl.hdp.exceptions;

public class HubDePagosException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1975293196780700281L;

	public HubDePagosException() {
		super("Exception, PedidosException Detectada!!!...");
	}

	public HubDePagosException(Throwable throwable) {
		super(throwable);
	}

	public HubDePagosException(String message) {
		super(message);
	}

	public HubDePagosException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public HubDePagosException(Exception e) {
		super(e.getMessage());
	}

}
