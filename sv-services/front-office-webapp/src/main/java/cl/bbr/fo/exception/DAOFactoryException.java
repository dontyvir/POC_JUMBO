/*
 * Created on 30-ene-2009
 */
package cl.bbr.fo.exception;

/**
 * @author jdroguett
 */
public class DAOFactoryException extends Exception {
    /**
     * @param message
     */
    public DAOFactoryException(String message) {
        super(message);
    }
    /**
     * @param message
     * @param cause
     */
    public DAOFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * @param cause
     */
    public DAOFactoryException(Throwable cause) {
        super(cause);
    }
    public DAOFactoryException(){
    }
}
