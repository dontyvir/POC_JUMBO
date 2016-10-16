/*
 * Created on Jun 20, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.ventaslocales;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CargaCompraException extends InterfazVentasException {

	public static final int ERROR_EN_HEADER = 1;
	public static final int ERROR_EN_CLIENTE = 2;
	public static final int ERROR_EN_PRODUCTOS = 3;
	public static final int ERROR_EN_MEDIOS_DE_PAGO = 4;
	public static final int ERROR_GRABANDO_EN_VTHJM = 5;
	public static final int ERROR_INFORMACION_DESCONOCIDA = 6;
	public static final int ERROR_EN_CLIENTE_SALTAR_REGISTRO = 7;
	public static final int ERROR_EN_PRODUCTOS_SALTAR_REGISTRO = 8;
	public static final int ERROR_GRABANDO_EN_JMCL = 9;
	
	
	private int errorCode = 0; 

	/**
	 * @param message
	 * @param cause
	 */
	public CargaCompraException(String message, Throwable cause) {
		super(message, cause);
		// Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CargaCompraException(Throwable cause) {
		super(cause);
		// Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public CargaCompraException() {
		super();
		// Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public CargaCompraException(String arg0) {
		super(arg0);
		// Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public CargaCompraException(String arg0, int errorCode) {
		super(arg0);
		this.errorCode = errorCode;
	}

	public CargaCompraException(String message, int errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
