/*
 * Created on 13-11-2012
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bbr.promo.lib.exception;

import java.sql.SQLException;

/**
 * @author Administrador
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PromoDAOException extends Exception {

    /**
     * @param string
     * @param e
     */
    public PromoDAOException(String string, SQLException e) {
        
        super(e);

    }

}
