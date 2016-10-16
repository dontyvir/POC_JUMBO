package cl.jumbo.ventamasiva.exceptions;

public class FuncionalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FuncionalException() {
		super("Exception, FuncionalException Detectada!!!...");
	}

	public FuncionalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FuncionalException(String arg0) {
		super(arg0);
	}

	public FuncionalException(Throwable arg0) {
		super(arg0);
	}	
}
