/*
 * Created on 14-mar-2008
 */
package cl.cencosud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author jdroguett
 */
public class Db {
    public static Connection conexion(String user, String password, String driver, String url) throws Exception {
        Connection con = null;
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (ClassNotFoundException e) {
                throw new Exception("Error: El driver no se encuentra");
            }
            Properties p = new Properties();
            p.setProperty("user", user);
            p.setProperty("password", password);

            if ((con = DriverManager.getConnection(url, p)) == null) {
                throw new Exception("Error: la conexion es nula");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener nueva conexi�n: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error al obtener nueva conexi�n: " + e.getMessage());
        }
        return con;
    }
}
