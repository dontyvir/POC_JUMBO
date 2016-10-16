/*
 * Created on 17-01-2008
 *
 */
package cl.cencosud.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author jdroguett
 *
 */

public class Parametros {

    private static String BUNDLE_NAME = "ranking";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return '!' + key + '!';
        }
    }

    public static void setNameProperties(String s) {
        Parametros.BUNDLE_NAME = s;
    }
}
