/**
 * PromocionDAOException.java
 * Creado   : 01-jun-2007
 * Historia : 21-jun-2007 Version 1.1
 * Version  : 1.0
 * BBR
 */
package cl.bbr.irs.promolib.exceptions;

/**
 * @author JORGE
 *
 */
public class IrsPromocionDAOException extends Exception {

	public IrsPromocionDAOException() {
		super("Exception, IrsPromocionDAOException Detectada!!!...");
	}

	public IrsPromocionDAOException(Throwable throwable) {
		super(throwable);
	}

	public IrsPromocionDAOException(String message) {
		super(message);
	}

	public IrsPromocionDAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IrsPromocionDAOException(Exception e) {
		super(e.getMessage());
	}	

}
