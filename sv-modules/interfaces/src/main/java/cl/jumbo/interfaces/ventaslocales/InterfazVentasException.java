package cl.jumbo.interfaces.ventaslocales;

/**
 * Exception general arrojada por la Interfaz de Ventas
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.2 - 15/05/2006
 *
 */
public class InterfazVentasException extends Exception {

	public InterfazVentasException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterfazVentasException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -8185702374336593046L;

	public InterfazVentasException() {
		super();
	}

	public InterfazVentasException(String arg0) {
		super(arg0);
	}
}
