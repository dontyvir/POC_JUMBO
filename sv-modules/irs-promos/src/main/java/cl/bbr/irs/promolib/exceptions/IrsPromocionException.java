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
public class IrsPromocionException extends Exception {

	public IrsPromocionException() {
		super("Exception, IrsPromocionException Detectada!!!...");
	}

	public IrsPromocionException(Throwable throwable) {
		super(throwable);
	}

	public IrsPromocionException(String message) {
		super(message);
	}

	public IrsPromocionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IrsPromocionException(Exception e) {
		super(e.getMessage());
	}	

}
