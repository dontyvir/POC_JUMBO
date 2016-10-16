
package cl.cencosud.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author jdroguett
 * 
 */

public class Parametros {
    
    private static Logger logger = Logger.getLogger(Parametros.class);

    private static String BUNDLE_NAME = "variables";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString( String key ) {

        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            logger.error("MissingResourceException:"+e.getMessage()+" for key ["+key+"]");
            return '!' + key + '!';
        }
    }

    public static void setNameProperties( String s ) {

        Parametros.BUNDLE_NAME = s;
    }
}
